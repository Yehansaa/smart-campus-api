package com.campus.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Map;
import java.util.HashMap;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response getInfo() {

        Map<String, Object> response = new HashMap<>();
    
        response.put("name", "Smart Campus API");
        response.put("version", "1.0");
        response.put("status", "running");
        response.put("timestamp", System.currentTimeMillis());
        response.put("contact", "admin@smartcampus.com");
    
        Map<String, Object> resources = new HashMap<>();
    
        Map<String, String> rooms = new HashMap<>();
        rooms.put("href", "/api/v1/rooms");
        rooms.put("method", "GET");
    
        Map<String, String> sensors = new HashMap<>();
        sensors.put("href", "/api/v1/sensors");
        sensors.put("method", "GET");
    
        resources.put("rooms", rooms);
        resources.put("sensors", sensors);
    
        response.put("resources", resources);
    
        return Response.ok(response).build();
    }
}
