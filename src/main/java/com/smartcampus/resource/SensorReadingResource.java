package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * SUB-RESOURCE for managing sensor readings.
 * Instantiated by SensorResource's sub-resource locator.
 * NO @Path annotation at class level!
 */
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    private final String sensorId;  // Context from parent resource

    /**
     * Constructor receives sensorId from parent resource locator.
     * This is the KEY to the sub-resource pattern!
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * GET /api/v1/sensors/{sensorId}/readings
     */
    @GET
    public Response getAllReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>());
        
        // Sort by timestamp (newest first)
        List<SensorReading> sortedList = new ArrayList<>(sensorReadings);
        sortedList.sort((r1, r2) -> Long.compare(r2.getTimestamp(), r1.getTimestamp()));
        
        return Response.ok(sortedList).build();
    }

    /**
     * POST /api/v1/sensors/{sensorId}/readings
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) throws SensorUnavailableException {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        // Validate sensor status
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }

        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }

        // Auto-generate ID if not provided
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        // Set timestamp if not provided
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        reading.setSensorId(sensorId);
        DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        // ✅ CRITICAL: Update sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    /**
     * GET /api/v1/sensors/{sensorId}/readings/{readingId}
     */
    @GET
    @Path("/{readingId}")
    public Response getReadingById(@PathParam("readingId") String readingId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>());
        
        SensorReading reading = sensorReadings.stream()
                .filter(r -> r.getId().equals(readingId))
                .findFirst()
                .orElse(null);
        
        if (reading == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Reading not found");
            return Response.status(404).entity(error).build();
        }

        return Response.ok(reading).build();
    }

    /**
     * DELETE /api/v1/sensors/{sensorId}/readings/{readingId}
     */
    @DELETE
    @Path("/{readingId}")
    public Response deleteReading(@PathParam("readingId") String readingId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>());
        
        boolean removed = sensorReadings.removeIf(r -> r.getId().equals(readingId));
        
        if (!removed) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Reading not found");
            return Response.status(404).entity(error).build();
        }

        return Response.noContent().build();
    }
}
