package com.example.lab4.simulation.controller.events;

public class SimulationResetEvent implements SimulationEvent {

    private final long timestamp;

    public SimulationResetEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "SimulationResetEvent{" +
                "timestamp=" + timestamp +
                '}';
    }
}
