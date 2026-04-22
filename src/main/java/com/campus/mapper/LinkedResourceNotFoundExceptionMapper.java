package com.campus.mapper;

import jakarta.ws.rs.ext.*;
import jakarta.ws.rs.core.Response;
import java.util.Map;

import com.campus.exception.LinkedResourceNotFoundException;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        return Response.status(422)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}
