package com.example.lab4.simulation.persistence.dto;

import java.util.List;

public class TerrainJsonDTO {

    public static class Point {
        public double x;
        public double h;
    }

    public List<Point> points;
}
