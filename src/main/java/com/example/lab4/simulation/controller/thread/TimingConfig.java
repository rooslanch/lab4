package com.example.lab4.simulation.controller.thread;

public class TimingConfig {
    private final double dt;   // шаг интегрирования, секунды

    public TimingConfig(double dt) {
        this.dt = dt;
    }

    public double getDt() {
        return dt;
    }
}