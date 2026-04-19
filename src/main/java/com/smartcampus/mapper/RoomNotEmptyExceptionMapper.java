package com.smartcampus.mapper;

import javax.ws.rs.ext.*;
import javax.ws.rs.core.Response;
import java.util.Map;

import com.smartcampus.exception.RoomNotEmptyException;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}