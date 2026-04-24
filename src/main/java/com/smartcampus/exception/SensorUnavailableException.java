package com.smartcampus.exception;

public class SensorUnavailableException extends Exception {
    private String sensorId;
    private String status;

    public SensorUnavailableException(String sensorId, String status) {
        super("Sensor " + sensorId + " is currently in " + status + 
              " status and cannot accept new readings");
        this.sensorId = sensorId;
        this.status = status;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getStatus() {
        return status;
    }
}
