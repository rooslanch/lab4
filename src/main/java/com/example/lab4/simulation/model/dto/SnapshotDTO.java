package com.example.lab4.simulation.model.dto;

public class SnapshotDTO {
    private final double x;
    private final double v;
    private final double a;
    private final double slope;
    private final double height;
    private final double timestamp;

    public SnapshotDTO(double x, double v, double a, double slope, double height, double timestamp) {
        this.x = x;
        this.v = v;
        this.a = a;
        this.slope = slope;
        this.height = height;
        this.timestamp = timestamp;
    }

    public double getX() { return x; }
    public double getV() { return v; }
    public double getA() { return a; }
    public double getSlope() { return slope; }
    public double getHeight() { return height; }
    public double getTimestamp() { return timestamp; }
}
