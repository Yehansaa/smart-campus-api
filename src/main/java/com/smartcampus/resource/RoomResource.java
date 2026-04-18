@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getAllRooms() {
        return DataStore.rooms.values();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        DataStore.rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .header("Location", "/api/v1/rooms/" + room.getId())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            return Response.status(409)
                    .entity(Map.of("error", "Room has active sensors"))
                    .build();
        }

        DataStore.rooms.remove(id);
        return Response.noContent().build();
    }
}