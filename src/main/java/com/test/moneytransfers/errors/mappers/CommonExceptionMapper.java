package com.test.moneytransfers.errors.mappers;

import com.test.moneytransfers.errors.NotFoundException;
import com.test.moneytransfers.errors.TransferException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class CommonExceptionMapper implements ExceptionMapper<Exception> {

    private final Map<Class<?>, Integer> statuses = new HashMap<>(){{
        put(NotFoundException.class, 404);
        put(TransferException.class, 422);
    }};

    public Response toResponse(Exception e) {
        return Response.status(statuses.getOrDefault(e.getClass(), 500))
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}