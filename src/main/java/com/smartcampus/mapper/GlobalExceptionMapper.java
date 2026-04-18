@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable ex) {
        return Response.status(500)
                .entity(Map.of("error", "Internal server error"))
                .build();
    }
}