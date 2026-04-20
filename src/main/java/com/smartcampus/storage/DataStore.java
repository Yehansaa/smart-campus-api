package com.smartcampus.storage;

import java.util.*;
import com.smartcampus.model.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataStore {
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}