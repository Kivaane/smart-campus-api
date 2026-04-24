package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) throws com.smartcampus.exception.LinkedResourceNotFoundException {
        if (sensor == null || sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor ID cannot be null or empty");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (DataStore.sensors.containsKey(sensor.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor already exists with this ID");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room ID must be provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new com.smartcampus.exception.LinkedResourceNotFoundException("Room", sensor.getRoomId());
        }

        // Add sensor to DataStore
        DataStore.sensors.put(sensor.getId(), sensor);

        // Update Room.sensorIds explicitly mapping relationships safely
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        // Add Location header
        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());

        // Provide filtering when @QueryParam("type") is supplied
        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor s : allSensors) {
                if (type.equalsIgnoreCase(s.getType())) {
                    filtered.add(s);
                }
            }
            return Response.ok(filtered).build();
        }

        // No type provided, return all
        return Response.ok(allSensors).build();
    }

    /**
     * SUB-RESOURCE LOCATOR
     * Path: /api/v1/sensors/{sensorId}/readings
     * 
     * This method does NOT handle requests itself.
     * It delegates to SensorReadingResource.
     */
    @Path("{sensorId}/readings")
    public SensorReadingResource getReadingsSubResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
