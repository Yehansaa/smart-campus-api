package com.campus.mapper;

import jakarta.ws.rs.ext.*;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import jakarta.ws.rs.WebApplicationException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {

        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "error", "Internal Server Error",
                        "message", "Something went wrong"
                ))
                .build();
    }
}
