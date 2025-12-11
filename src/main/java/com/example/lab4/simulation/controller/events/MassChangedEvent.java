package com.example.lab4.simulation.controller.events;

public class MassChangedEvent implements SimulationEvent {

    private final double newMass;
    private final long timestamp;

    public MassChangedEvent(double newMass) {
        this.newMass = newMass;
        this.timestamp = System.currentTimeMillis();
    }

    public double getNewMass() {
        return newMass;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "MassChangedEvent{" +
                "newMass=" + newMass +
                ", timestamp=" + timestamp +
                '}';
    }
}
