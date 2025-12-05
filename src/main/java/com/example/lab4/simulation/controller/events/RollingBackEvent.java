package com.example.lab4.simulation.controller.events;

public class RollingBackEvent implements SimulationEvent {

    private final double x;
    private final double v;
    private final double slope;
    private final long timestamp;

    public RollingBackEvent(double x, double v, double slope) {
        this.x = x;
        this.v = v;
        this.slope = slope;
        this.timestamp = System.currentTimeMillis();
    }


    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public double getX() { return x; }
    public double getV() { return v; }
    public double getSlope() { return slope; }
}
