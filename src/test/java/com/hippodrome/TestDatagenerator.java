package com.hippodrome;

import java.util.LinkedList;
import java.util.List;

class TestDataGenerator {
    // Default horse params
    static final String DEFAULT_HORSE_NAME = "Thunder";
    static final double DEFAULT_SPEED = 10.0;
    static final double DEFAULT_DISTANCE = 20.0;

    static List<Horse> createRandomHorses(int count) {
        List<Horse> horses = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            horses.add(new Horse(DEFAULT_HORSE_NAME + i, DEFAULT_SPEED + i, DEFAULT_DISTANCE + i));
        }
        return horses;
    }
}
