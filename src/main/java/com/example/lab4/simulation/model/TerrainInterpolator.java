package com.example.lab4.simulation.model;

import java.util.List;

public class TerrainInterpolator {

    private final List<TerrainPoint> points;
    public TerrainInterpolator(List<TerrainPoint> points) {
        this.points = points;
    }

    /** Линейная интерполяция высоты */
    public double getHeight(double x) {
        Segment seg = findSegment(x);
        return interpolateHeight(x, seg);
    }

    /** Уклон dh/dx – НЕ угол, просто отношение. Угол = atan(slope). */
    public double getSlope(double x) {
        Segment seg = findSegment(x);
        return (seg.h1 - seg.h0) / (seg.x1 - seg.x0);
    }


    /** Ищем между какими точками находится x */
    private Segment findSegment(double x) {


        // за пределами – клампим в ближайший сегмент
        if (x <= points.get(0).getX()) {
            TerrainPoint p0 = points.get(0);
            TerrainPoint p1 = points.get(1);
            return new Segment(p0.getX(), p0.getH(), p1.getX(), p1.getH());
        }
        if (x >= points.get(points.size() - 1).getX()) {
            TerrainPoint p0 = points.get(points.size() - 2);
            TerrainPoint p1 = points.get(points.size() - 1);
            return new Segment(p0.getX(), p0.getH(), p1.getX(), p1.getH());
        }

        // нормальный случай
        for (int i = 0; i < points.size() - 1; i++) {
            TerrainPoint p0 = points.get(i);
            TerrainPoint p1 = points.get(i + 1);

            if (x >= p0.getX() && x <= p1.getX()) {
                return new Segment(p0.getX(), p0.getH(), p1.getX(), p1.getH());
            }
        }

        throw new IllegalStateException("Segment should always be found");
    }

    /** Линейная интерполяция по сегменту */
    private double interpolateHeight(double x, Segment s) {
        double t = (x - s.x0) / (s.x1 - s.x0); // сколько пройдено по сегменту
        return s.h0 + t * (s.h1 - s.h0); // h0 + t * разница_высот
    }

    /** Упрощённая структура сегмента */
    private static class Segment {
        final double x0, h0;
        final double x1, h1;

        Segment(double x0, double h0, double x1, double h1) {
            this.x0 = x0;
            this.h0 = h0;
            this.x1 = x1;
            this.h1 = h1;
        }
    }
}
