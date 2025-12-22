package com.example.lab4.simulation.controller.thread;

import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.controller.events.*;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;

public class SimulationLoop implements Runnable {

    private final PhysicsEngine engine;
    private final PhysicsModel model;
    private final Terrain terrain;

    private final SimulationControllerImpl controller;
    private final TimingConfig timing;
    private double lastFriction = Double.NaN;

    private volatile boolean running = false;
    private volatile boolean paused = false;

    public SimulationLoop(
            PhysicsEngine engine,
            PhysicsModel model,
            Terrain terrain,
            SimulationControllerImpl controller,
            TimingConfig timing
    ) {
        this.engine = engine;
        this.model = model;
        this.terrain = terrain;
        this.controller = controller;
        this.timing = timing;
    }


    public void start() {
        running = true;
        Thread t = new Thread(this, "SimulationLoop");
        t.setDaemon(true);
        t.start();
    }


    public void stop() {
        running = false;
    }


    public void setPaused(boolean value) {
        this.paused = value;
    }

    public boolean isRunning() {
        return running;
    }

    public double getA() {
        return engine.getCurrentAcceleration();
    }

    @Override
    public void run() {
        long frameNanos = (long) (1e9 * timing.getDt());
        long nextTime = System.nanoTime();

        while (running) {
            if (!paused) {


                engine.update(timing.getDt());

                double x = model.getX();
                double v = model.getV();
                double slope = engine.getCurrentSlope();
                double friction = terrain.getFrictionAt(x);

                if (Double.isNaN(lastFriction) || friction != lastFriction) {
                    lastFriction = friction;
                    controller.dispatchEvent(new FrictionChangedEvent(friction));
                }


                if (x <= terrain.getMinX()) {
                    model.setX(terrain.getMinX());
                    if (v < 0) model.setV(0);
                    controller.dispatchEvent(new BoundaryReachedEvent(BoundaryType.START, terrain.getMinX()));
                } else if (x >= terrain.getMaxX()) {
                    model.setX(terrain.getMaxX());
                    if (v > 0) model.setV(0);
                    controller.dispatchEvent(new BoundaryReachedEvent(BoundaryType.END, terrain.getMaxX()));
                }


                if (v < 0 && x > terrain.getMinX()) {
                    controller.dispatchEvent(new RollingBackEvent(x, v, slope));
                }


                controller.dispatchEvent(
                        new StateUpdateEvent(
                                model.getX(),
                                model.getV(),
                                engine.getCurrentAcceleration(),
                                terrain.getHeightAt(model.getX()),
                                engine.getCurrentSlope()
                        )
                );
            }


            nextTime += frameNanos;
            long sleep = nextTime - System.nanoTime();
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep / 1_000_000, (int) (sleep % 1_000_000));
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
