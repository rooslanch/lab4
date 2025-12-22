package com.example.lab4.simulation.controller.events;

public class StateUpdateEvent implements SimulationEvent {

    private final double x;
    private final double v;
    private final double a;
    private final double h;
    private final double slope;
    private final long timestamp;

    public StateUpdateEvent(double x, double v, double a, double h, double slope) {
        this.x = x;
        this.v = v;
        this.a = a;
        this.h = h;
        this.slope = slope;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "StateUpdateEvent{" +
                "a=" + a +
                ", x=" + x +
                ", v=" + v +
                ", slope=" + slope +
                ", timestamp=" + timestamp +
                '}';
    }

    public double getH() {
        return h;
    }

    public double getX() {
        return x;
    }

    public double getV() {
        return v;
    }

    public double getA() {
        return a;
    }

    public double getSlope() {
        return slope;
    }
}
