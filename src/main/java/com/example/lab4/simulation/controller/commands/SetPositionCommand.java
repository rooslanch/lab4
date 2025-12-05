package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.PositionChangedEvent;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;

/**
 * Устанавливает координату модели (например, при jump-to-position в GUI).
 */
public class SetPositionCommand implements ControlCommand {

    private final double newX;

    @Override
    public void execute(SimulationController controller) {
        controller.getModel().setX(newX);
        // При желании можно добавить событие изменения позиции
        controller.dispatchEvent(new PositionChangedEvent(newX));
    }

    public SetPositionCommand(double newX) {
        this.newX = newX;
    }

    public double getNewX() {
        return newX;
    }

    @Override
    public String toString() {
        return "SetPositionCommand{x=" + newX + '}';
    }
}
