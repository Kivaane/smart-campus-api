package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(DataStore.rooms.values());
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room ID cannot be null or empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (DataStore.rooms.containsKey(room.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "A room with the specified ID already exists.");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStore.rooms.put(room.getId(), room);

        // Add Location header pointing to the created resource
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Provided Room ID is invalid.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        Room room = DataStore.rooms.get(id);
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) throws com.smartcampus.exception.RoomNotEmptyException {
        Room room = DataStore.rooms.get(id);

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room with ID " + id + " not found");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new com.smartcampus.exception.RoomNotEmptyException(id, room.getSensorIds().size());
        }

        DataStore.rooms.remove(id);
        return Response.noContent().build();
    }
}
