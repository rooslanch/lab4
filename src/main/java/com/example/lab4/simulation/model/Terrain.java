package com.example.lab4.simulation.model;

import java.util.List;

public class Terrain {

    private final List<TerrainPoint> points;
    private final TerrainInterpolator interpolator;
    private final FrictionProfile frictionProfile;

    public Terrain(List<TerrainPoint> points, FrictionProfile frictionProfile) {
        if (points == null || points.size() < 2)
            throw new IllegalArgumentException("Terrain must contain at least 2 points");

        List<TerrainPoint> sorted = points.stream().sorted((a, b) -> Double.compare(a.getX(), b.getX())).toList();

        this.points = List.copyOf(sorted);
        this.interpolator = new TerrainInterpolator(this.points);
        this.frictionProfile = frictionProfile;
    }

    public double getFrictionAt(double x) {
        return frictionProfile.getFrictionAt(x);
    }

    public FrictionProfile getFrictionProfile() {
        return frictionProfile;
    }


    public List<TerrainPoint> getPoints() {
        return points;
    }

    public double getHeightAt(double x) {
        return interpolator.getHeight(x);
    }

    public double getSlopeAt(double x) {
        return interpolator.getSlope(x);
    }

    /**
     * Минимальный X – начало дороги
     */
    public double getMinX() {
        return points.get(0).getX();
    }

    /**
     * Максимальный X – конец дороги
     */
    public double getMaxX() {
        return points.get(points.size() - 1).getX();
    }
}
