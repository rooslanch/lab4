package com.example.lab4.simulation.controller.events;

public class FrictionChangedEvent implements SimulationEvent {

    private final double newFriction;
    private final long timestamp = System.currentTimeMillis();

    public FrictionChangedEvent(double newFriction) {
        this.newFriction = newFriction;
    }

    public double getNewFriction() {
        return newFriction;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "FrictionChangedEvent{" +
                "newFriction=" + newFriction +
                ", timestamp=" + timestamp +
                '}';
    }
}

