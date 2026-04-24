package com.smartcampus.model;

import java.util.Objects;
import java.util.UUID;

public class SensorReading {
    private String id;
    private long timestamp;
    private double value;
    private String sensorId;

    public SensorReading() {
    }

    public SensorReading(long timestamp, double value, String sensorId) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.value = value;
        this.sensorId = sensorId;
    }

    public SensorReading(String id, long timestamp, double value, String sensorId) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.sensorId = sensorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorReading that = (SensorReading) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                ", sensorId='" + sensorId + '\'' +
                '}';
    }
}
