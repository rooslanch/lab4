package com.example.lab4.simulation.model;

public class PhysicsEngine {

    private final Terrain terrain;
    private final PhysicsModel model;

    private double throttleForce; // текущая сила тяги (команда от контроллера)

    public double g = 9.81;

    public PhysicsEngine(Terrain terrain, PhysicsModel model) {
        this.terrain = terrain;
        this.model = model;
    }

    public void setThrottleForce(double force) {
        this.throttleForce = force;
    }

    /**
     * Выполняет один шаг физики.
     *
     * @param dt шаг интегрирования в секундах (например, 0.016 = 60 Гц)
     */
    public void update(double dt) {

        double x = model.getX();
        double v = model.getV();
        double m = model.getMass();

        double slope = terrain.getSlopeAt(x);
        double mu = terrain.getFrictionAt(x);

        double cosTheta = 1.0 / Math.sqrt(1 + slope * slope);

        double gravityComponent =
                m * g * slope / Math.sqrt(1 + slope * slope);

        double normalForce = m * g * cosTheta;
        double frictionForce = mu * normalForce * Math.signum(v);

        double netForce =
                throttleForce
                        - gravityComponent
                        - frictionForce;

        double a = netForce / m;

        double newV = v + a * dt;
        double newX = x + newV * dt;

        model.setV(newV);
        model.setX(newX);
    }

    public void setGravity(double g) {
        this.g = g;
    }

    public double getGravity() {
        return g;
    }


    /**
     * Текущие данные для отображения.
     */
    public double getCurrentSlope() {
        return terrain.getSlopeAt(model.getX());
    }

    public double getCurrentAcceleration() {
        double slope = terrain.getSlopeAt(model.getX());
        double gravityComponent = model.getMass() * g * slope / Math.sqrt(1 + slope * slope);
        return (throttleForce - gravityComponent) / model.getMass();
    }
}

