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
    return Response.status(Response.Status.CREATED).entity(room).build();
}

@GET
@Path("/{id}")
public Response getRoom(@PathParam("id") String id) {
    Room room = DataStore.rooms.get(id);

    if (room == null) {
        return Response.status(404).entity("Room not found").build();
    }

    return Response.ok(room).build();
}

@DELETE
@Path("/{id}")
public Response deleteRoom(@PathParam("id") String id) {
    Room room = DataStore.rooms.get(id);

    if (room == null) {
        return Response.status(404).build();
    }

    if (!room.getSensorIds().isEmpty()) {
        return Response.status(409).entity("Room has sensors").build();
    }

    DataStore.rooms.remove(id);
    return Response.noContent().build();
}