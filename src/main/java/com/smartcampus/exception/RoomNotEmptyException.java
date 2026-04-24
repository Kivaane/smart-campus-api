package com.smartcampus.exception;

public class RoomNotEmptyException extends Exception {
    private String roomId;
    private int sensorCount;

    public RoomNotEmptyException(String roomId, int sensorCount) {
        super("Cannot delete room " + roomId + " because it still has " + 
              sensorCount + " active sensor(s)");
        this.roomId = roomId;
        this.sensorCount = sensorCount;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getSensorCount() {
        return sensorCount;
    }
}
