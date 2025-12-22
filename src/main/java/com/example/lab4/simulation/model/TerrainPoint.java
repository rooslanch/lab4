package com.example.lab4.simulation.model;

// минимальный иммутабельный POJO
public class TerrainPoint {
    private final double x;
    private final double h;

    public TerrainPoint(double x, double h) {
        this.x = x;
        this.h = h;
    }

    public double getX() {
        return x;
    }

    public double getH() {
        return h;
    }
}
