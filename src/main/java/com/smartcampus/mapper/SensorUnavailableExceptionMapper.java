@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(403)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}