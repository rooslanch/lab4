package com.example.lab4.simulation.controller.events;

public class ThrottleChangedEvent implements SimulationEvent {

    private final double newThrottleForce;
    private final long timestamp;

    public ThrottleChangedEvent(double newThrottleForce) {
        this.newThrottleForce = newThrottleForce;
        this.timestamp = System.currentTimeMillis();
    }

    public double getNewThrottleForce() {
        return newThrottleForce;
    }

    @Override
    public String toString() {
        return "ThrottleChangedEvent{" +
                "newThrottleForce=" + newThrottleForce +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
