package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.storage.DataStore;
import com.smartcampus.exception.SensorUnavailableException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found");
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance");
        }

        DataStore.readings
                .computeIfAbsent(sensorId, k -> new CopyOnWriteArrayList<>())
                .add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}