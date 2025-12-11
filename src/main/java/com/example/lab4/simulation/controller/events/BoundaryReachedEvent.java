package com.example.lab4.simulation.controller.events;

public class BoundaryReachedEvent implements SimulationEvent {


    private final BoundaryType type;
    private final double position;
    private final long timestamp;

    public BoundaryReachedEvent(BoundaryType type, double position) {
        this.type = type;
        this.position = position;
        this.timestamp = System.currentTimeMillis();
    }

    public BoundaryType getType() {
        return type;
    }

    public double getX() {
        return position;
    }

    @Override
    public String toString() {
        return "BoundaryReachedEvent{" +
                "position=" + position +
                ", boundary type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
