package com.smartcampus.data;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Map<String, List<SensorReading>> getReadings() {
        return readings;
    }

    public static List<SensorReading> getReadingsForSensor(String sensorId) {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    public static void addReading(String sensorId, SensorReading reading) {
        readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }

    public static SensorReading getReadingById(String sensorId, String readingId) {
        List<SensorReading> sensorReadings = readings.get(sensorId);
        if (sensorReadings == null) {
            return null;
        }
        
        return sensorReadings.stream()
                .filter(r -> r.getId().equals(readingId))
                .findFirst()
                .orElse(null);
    }

    public static boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public static boolean sensorExists(String sensorId) {
        return sensors.containsKey(sensorId);
    }

    public static boolean isSensorActive(String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            return false;
        }
        return "ACTIVE".equalsIgnoreCase(sensor.getStatus());
    }
}
