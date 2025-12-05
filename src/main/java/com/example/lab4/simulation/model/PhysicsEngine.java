package com.example.lab4.simulation.model;

public class PhysicsEngine {

    private final Terrain terrain;
    private final PhysicsModel model;

    private double throttleForce; // текущая сила тяги (команда от контроллера)

    public static final double G = 9.81;

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

        // 1. Вычислить наклон k = dh/dx
        double slope = terrain.getSlopeAt(x);

        // 2. Проекция g вдоль наклона
        double gravityComponent = m * G * slope / Math.sqrt(1 + slope * slope);

        // 3. Суммарная сила
        double netForce = throttleForce - gravityComponent;

        // 4. Ускорение
        double a = netForce / m;

        // 5. Интегрирование скорости и координаты
        double newV = v + a * dt;
        double newX = x + newV * dt;

        model.setV(newV);
        model.setX(newX);
    }

    /**
     * Текущие данные для отображения.
     */
    public double getCurrentSlope() {
        return terrain.getSlopeAt(model.getX());
    }

    public double getCurrentAcceleration() {
        double slope = terrain.getSlopeAt(model.getX());
        double gravityComponent = model.getMass() * G * slope / Math.sqrt(1 + slope * slope);
        return (throttleForce - gravityComponent) / model.getMass();
    }
}

