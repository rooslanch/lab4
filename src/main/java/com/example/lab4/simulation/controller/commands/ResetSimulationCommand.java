package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.SimulationResetEvent;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;

/**
 * Полный сброс симуляции в начальное состояние.
 */
public class ResetSimulationCommand implements ControlCommand {

    @Override
    public void execute(SimulationController controller) {
        controller.getModel().reset();
        controller.getPhysicsEngine().setThrottleForce(0);
        controller.dispatchEvent(new SimulationResetEvent());
    }
    @Override
    public String toString() {
        return "ResetSimulationCommand{}";
    }
}