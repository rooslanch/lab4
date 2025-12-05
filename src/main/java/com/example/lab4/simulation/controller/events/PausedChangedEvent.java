package com.example.lab4.simulation.controller.events;

public class PausedChangedEvent implements SimulationEvent {

    private final boolean paused;
    private final long timestamp;

    public PausedChangedEvent(boolean paused) {
        this.paused = paused;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isPaused() {
        return paused;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PausedChangedEvent{paused=" + paused + ", timestamp=" + timestamp + '}';
    }
}
