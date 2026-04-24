package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getMessage());
        errorResponse.put("resourceType", exception.getResource());
        errorResponse.put("resourceId", exception.getResourceId());

        return Response.status(422)  // 422 Unprocessable Entity
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
