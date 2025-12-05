package com.example.lab4.simulation.controller.events;

public class PositionChangedEvent implements SimulationEvent {

    private final double x;
    private final long timestamp;

    public PositionChangedEvent(double x) {
        this.x = x;
        this.timestamp = System.currentTimeMillis();
    }

    public double getX() {
        return x;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PositionChangedEvent{x=" + x + ", timestamp=" + timestamp + '}';
    }
}
