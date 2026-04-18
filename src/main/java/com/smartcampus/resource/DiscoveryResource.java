@Path("/")
@Produces(MediaType.APPLICATION_JSON)

public class DiscoveryResource {

    @GET
    public Map<String, Object> getInfo() {
        Map<String, Object> response = new HashMap<>();

        response.put("version", "1.0");
        response.put("contact", "admin@smartcampus.com");

        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        response.put("resources", resources);

        return response;
    }
}