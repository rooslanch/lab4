package com.example.lab4.simulation.controller.events;

public class GravityChangedEvent implements SimulationEvent {

    private final double newGravity;
    private final long timestamp;

    public GravityChangedEvent(double newGravity) {
        this.newGravity = newGravity;
        this.timestamp = System.currentTimeMillis();
    }

    public double getNewGravity() {
        return newGravity;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "GravityChangedEvent{" +
                "newGravity=" + newGravity +
                ", timestamp=" + timestamp +
                '}';
    }
}
