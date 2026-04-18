@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(409)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}