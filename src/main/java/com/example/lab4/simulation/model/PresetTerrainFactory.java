package com.example.lab4.simulation.model;

import java.util.ArrayList;
import java.util.List;

public class PresetTerrainFactory {

    public static Terrain createFlat(double length, double height) {
        List<TerrainPoint> points = List.of(
                new TerrainPoint(0, height),
                new TerrainPoint(length, height)
        );
        return new Terrain(points);
    }

    public static Terrain createHump(double length, double height) {
        List<TerrainPoint> points = new ArrayList<>();
        points.add(new TerrainPoint(0, 0));
        points.add(new TerrainPoint(length / 2, height));
        points.add(new TerrainPoint(length, 0));
        return new Terrain(points);
    }

    public static Terrain createRandom(double length, double maxHeight, int numPoints) {
        List<TerrainPoint> points = new ArrayList<>();
        double step = length / (numPoints - 1);
        for (int i = 0; i < numPoints; i++) {
            double x = i * step;
            double y = Math.random() * maxHeight;
            points.add(new TerrainPoint(x, y));
        }
        return new Terrain(points);
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
        return new Terrain(points);
    }
}
