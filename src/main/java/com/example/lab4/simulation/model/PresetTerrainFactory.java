package com.example.lab4.simulation.model;

import java.util.ArrayList;
import java.util.List;

public class PresetTerrainFactory {

    public static Terrain create(TerrainPreset preset) {
        return switch (preset) {
            case FLAT -> createFlat(50, 0);
            case HUMP -> createHump(50, 5);
            case RANDOM -> createRandom(50, 5, 20);
            case WAVE -> createWave(50, 2, 2);
        };
    }

    public static Terrain createFlat(double length, double height) {
        List<TerrainPoint> points = List.of(
                new TerrainPoint(0, height),
                new TerrainPoint(length, height)
        );
        FrictionProfile friction = new FrictionProfile();
        friction.addSection(0.0, 0.02); // весь участок — слабое трение

        return new Terrain(points, friction);
    }

    public static Terrain createHump(double length, double height) {
        List<TerrainPoint> points = new ArrayList<>();
        points.add(new TerrainPoint(0, 0));
        points.add(new TerrainPoint(length / 2, height));
        points.add(new TerrainPoint(length, 0));
        FrictionProfile friction = new FrictionProfile();
        friction.addSection(0.0, 0.02);                 // начало — асфальт
        friction.addSection(length / 2, 0.08);          // горка — грязь

        return new Terrain(points, friction);
    }

    public static Terrain createRandom(double length, double maxHeight, int numPoints) {
        List<TerrainPoint> points = new ArrayList<>();
        double step = length / (numPoints - 1);
        for (int i = 0; i < numPoints; i++) {
            double x = i * step;
            double y = Math.random() * maxHeight;
            points.add(new TerrainPoint(x, y));
        }
        FrictionProfile friction = new FrictionProfile();
        friction.addSection(0.0, 0.03);
        friction.addSection(length * 0.3, 0.06);
        friction.addSection(length * 0.7, 0.01);

        return new Terrain(points, friction);
    }

    // Можно добавить любые фигурные пресеты
    public static Terrain createWave(double length, double amplitude, int waves) {
        List<TerrainPoint> points = new ArrayList<>();
        int numPoints = waves * 20;
        double step = length / (numPoints - 1);
        for (int i = 0; i < numPoints; i++) {
            double x = i * step;
            double y = Math.sin(i * 2 * Math.PI / (numPoints / waves)) * amplitude;
            points.add(new TerrainPoint(x, y));
        }
        FrictionProfile friction = new FrictionProfile();
        friction.addSection(0.0, 0.02);
        friction.addSection(length * 0.25, 0.1);
        friction.addSection(length * 0.5, 0.4);
        friction.addSection(length * 0.75, 0.8);

        return new Terrain(points, friction);
    }
}
