@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            return Response.status(422)
                    .entity(Map.of("error", "Room does not exist"))
                    .build();
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        DataStore.rooms.get(sensor.getRoomId())
                .getSensorIds()
                .add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return DataStore.sensors.values();
        }

        return DataStore.sensors.values()
                .stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .toList();
    }
}