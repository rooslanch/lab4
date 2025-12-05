package com.example.lab4.simulation.controller;



import com.example.lab4.simulation.controller.commands.ControlCommand;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.controller.observers.SimpleObserverRegistration;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.controller.thread.SimulationLoop;
import com.example.lab4.simulation.controller.thread.TimingConfig;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationControllerImpl implements SimulationController {

    private final Terrain terrain;
    private final PhysicsModel model;
    private final PhysicsEngine physics;
    TimingConfig timing = new TimingConfig(0.016); // dt = 16 мс ~ 60 FPS

    private final List<SimulationObserver> observers = new CopyOnWriteArrayList<>();
    private SimulationLoop loop;

    public SimulationControllerImpl(Terrain terrain, PhysicsModel model) {
        this.terrain = terrain;
        this.model = model;
        this.physics = new PhysicsEngine(terrain, model);
    }

    public double getDt() {
        return timing.getDt();
    }
    // =========================================================================
    //   ЖИЗНЕННЫЙ ЦИКЛ СИМУЛЯЦИИ
    // =========================================================================

    @Override
    public synchronized void start() {
        if (loop != null && loop.isRunning()) return;

        loop = new SimulationLoop(physics, model, terrain, this, timing);
        loop.start();
    }

    @Override
    public synchronized void stop() {
        if (loop != null) {
            loop.stop();
            loop = null;
        }
    }

    // =========================================================================
    //                               КОМАНДЫ
    // =========================================================================

    @Override
    public void execute(ControlCommand command) {
        command.execute(this);
    }

    // =========================================================================
    //                        ВЗАИМОДЕЙСТВИЕ С OBSERVER-АМИ
    // =========================================================================

    /**
     * Кидаем наблюдателя в runnable для последующего коллбека remove
     * @param observer
     * @return new ObserverRegistration
     */
    @Override
    public ObserverRegistration addObserver(SimulationObserver observer) {
        observers.add(observer);

        return new SimpleObserverRegistration(() -> observers.remove(observer));
    }

    /** Вызывается SimulationLoop при возникновении событий */
    @Override
    public void dispatchEvent(SimulationEvent ev) {
        for (SimulationObserver obs : observers) {
            obs.onEvent(ev);
        }
    }

    // =========================================================================
    //         Методы, которые используются командами (SetThrottle и др.)
    // =========================================================================

    @Override
    public PhysicsEngine getPhysicsEngine() {
        return physics;
    }
    @Override
    public PhysicsModel getModel() {
        return model;
    }
    @Override
    public Terrain getTerrain() {
        return terrain;
    }
    @Override
    public SimulationLoop getSimulationLoop(){
        return loop;
    }
}
