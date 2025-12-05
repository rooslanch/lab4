package com.example.lab4.simulation.view.charts;


public class ChartTimebase {
    public final double dt;
    private double time = 0;

    public ChartTimebase(double dt) {
        this.dt = dt;
    }

    public double nextTime() {
        time += dt;
        return time;
    }

    public double getTime() {
        return time;
    }
}

