package com.example.lab4.simulation.model;

public class PhysicsModel {
    private double x;      // текущая координата вдоль рельефа
    private double v;      // скорость (может быть отрицательная)
    private final double m; // масса (постоянная)

    public PhysicsModel(double mass) {
        this.m = mass;
    }

    public double getX() { return x; }
    public double getV() { return v; }
    public double getMass() { return m; }

    public void setX(double x) { this.x = x; }
    public void setV(double v) { this.v = v; }

    public void reset() {
        this.x = 0;
        this.v = 0;
    }
}
