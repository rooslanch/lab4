package com.example.lab4.simulation.model;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class PiecewiseFrictionProfile implements FrictionProfile {

    private final NavigableMap<Double, Double> frictionByX = new TreeMap<>();

    /**
     * @param fromX начиная с этой координаты действует коэффициент friction
     */
    public void addSection(double fromX, double friction) {
        frictionByX.put(fromX, friction);
    }
    public NavigableMap<Double, Double> getSections() {
        return (NavigableMap<Double, Double>) Map.copyOf(frictionByX);
    }

    @Override
    public double getFrictionAt(double x) {
        var entry = frictionByX.floorEntry(x);
        if (entry == null) {
            return 0.0; // по умолчанию — без трения
        }
        return entry.getValue();
    }
}
