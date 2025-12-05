package com.example.lab4.simulation.model.dto;

import java.util.List;

/**
 * Передается View один раз при старте (hotswap)
 */
public class TerrainDTO {

    public static class Point {
        private final double x;
        private final double h;

        public Point(double x, double h) {
            this.x = x;
            this.h = h;
        }

        public double getX() { return x; }
        public double getH() { return h; }
    }

    private final List<Point> points;
    /**
     * Контроллер будет создавать этот DTO как:
     *
     * public TerrainDTO toDTO() {
     *     return new TerrainDTO(
     *         terrain.getPoints().stream()
     *             .map(p -> new TerrainDTO.Point(p.getX(), p.getH()))
     *             .toList()
     *     );
     * }
     */
    public TerrainDTO(List<Point> points) {
        this.points = List.copyOf(points);
    }

    public List<Point> getPoints() {
        return points;
    }
}
