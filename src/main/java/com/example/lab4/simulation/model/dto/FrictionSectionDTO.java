package com.example.lab4.simulation.model.dto;

public class FrictionSectionDTO {

    private final double fromX;
    private final double friction;

    public FrictionSectionDTO(double fromX, double friction) {
        this.fromX = fromX;
        this.friction = friction;
    }

    public double getFromX() {
        return fromX;
    }

    public double getFriction() {
        return friction;
    }
}
