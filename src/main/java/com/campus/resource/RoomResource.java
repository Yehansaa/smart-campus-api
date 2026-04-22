package com.campus.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Collection;

import com.campus.model.Room;
import com.campus.storage.DataStore;
import com.campus.exception.RoomNotEmptyException;

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

        if (room.getId() == null || room.getId().isEmpty()) {
            throw new BadRequestException("Room ID is required");
        }

        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(java.util.Map.of("error", "Room already exists"))
                    .build();
        }

        DataStore.rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(java.util.Map.of(
                        "message", "Room created successfully",
                        "roomId", room.getId()
                ))
                .header("Location", "/api/v1/rooms/" + room.getId())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            throw new NotFoundException("Room not found");
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            throw new NotFoundException("Room not found");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has active sensors");
        }

        DataStore.rooms.remove(id);
        return Response.noContent().build();
    }
}
