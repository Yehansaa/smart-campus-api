package com.smartcampus.mapper;

import javax.ws.rs.ext.*;
import javax.ws.rs.core.Response;
import java.util.Map;

import com.smartcampus.exception.SensorUnavailableException;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}