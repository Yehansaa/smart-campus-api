package com.campus.mapper;

import jakarta.ws.rs.ext.*;
import jakarta.ws.rs.core.Response;
import java.util.Map;

import com.campus.exception.RoomNotEmptyException;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}
