package com.campus.mapper;

import jakarta.ws.rs.ext.*;
import jakarta.ws.rs.core.Response;
import java.util.Map;

import com.campus.exception.SensorUnavailableException;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}
