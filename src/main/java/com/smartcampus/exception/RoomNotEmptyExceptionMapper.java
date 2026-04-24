package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getMessage());
        errorResponse.put("roomId", exception.getRoomId());
        errorResponse.put("sensorCount", exception.getSensorCount());
        errorResponse.put("suggestion", "Remove all sensors from this room before deletion");

        return Response.status(409)  // 409 Conflict
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
