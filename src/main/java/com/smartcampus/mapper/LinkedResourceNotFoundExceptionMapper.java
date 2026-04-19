package com.smartcampus.mapper;

import javax.ws.rs.ext.*;
import javax.ws.rs.core.Response;
import java.util.Map;

import com.smartcampus.exception.LinkedResourceNotFoundException;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        return Response.status(422)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}