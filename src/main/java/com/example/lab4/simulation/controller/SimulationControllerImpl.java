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
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.SnapshotDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.model.mapper.FrictionMapper;
import com.example.lab4.simulation.model.mapper.TerrainMapper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationControllerImpl implements SimulationController {

    private Terrain terrain;
    private final PhysicsModel model;
    private final PhysicsEngine physics;
    TimingConfig timing = new TimingConfig(0.016);

    private final List<SimulationObserver> observers = new CopyOnWriteArrayList<>();
    private SimulationLoop loop;

    public SimulationControllerImpl(Terrain terrain, PhysicsModel model) {
        this.terrain = terrain;
        this.model = model;
        this.physics = new PhysicsEngine(terrain, model);
    }

    @Override
    public TerrainDTO getTerrainDTO() {
        return TerrainMapper.toDTO(terrain);
    }

    @Override
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        this.physics.setTerrain(terrain);
    }

    @Override
    public FrictionDTO getFrictionDTO() {
        return FrictionMapper.toDTO(terrain.getFrictionProfile());
    }

    @Override
    public SnapshotDTO getSnapshotDTO() {
        double x = model.getX();
        double v = model.getV();
        double a = physics.getCurrentAcceleration();
        double slope = physics.getCurrentSlope();
        double height = terrain.getHeightAt(x);
        double t = System.currentTimeMillis();

        return new SnapshotDTO(x, v, a, slope, height, t);
    }

    @Override
    public double getDt() {
        return timing.getDt();
    }

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


    @Override
    public void execute(ControlCommand command) {
        command.execute(this);
    }


    @Override
    public ObserverRegistration addObserver(SimulationObserver observer) {
        observers.add(observer);

        return new SimpleObserverRegistration(() -> observers.remove(observer));
    }


    @Override
    public void dispatchEvent(SimulationEvent ev) {
        for (SimulationObserver obs : observers) {
            obs.onEvent(ev);
        }
    }

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
    public SimulationLoop getSimulationLoop() {
        return loop;
    }
}
