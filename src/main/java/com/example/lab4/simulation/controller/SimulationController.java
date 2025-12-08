package com.example.lab4.simulation.controller;

import com.example.lab4.simulation.controller.commands.ControlCommand;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.events.SimulationResetEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.controller.thread.SimulationLoop;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.dto.TerrainDTO;

public interface SimulationController {

    /** Запустить симуляцию (создает поток SimulationLoop) */
    void start();

    /** Остановить симуляцию (корректно завершает поток) */
    void stop();

    /** Выполнить команду (SetThrottle, ResetSimulation и т.д.) */
    void execute(ControlCommand command);

    /** Подписать наблюдателя (view) */
    ObserverRegistration addObserver(SimulationObserver observer);


    PhysicsModel getModel();

    PhysicsEngine getPhysicsEngine();

    void dispatchEvent(SimulationEvent simulationEvent);

    SimulationLoop getSimulationLoop();
    Terrain getTerrain();
    double getDt();
    TerrainDTO getTerrainDTO();
}