package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;

/**
 * Интерфейс команды управления симуляцией.
 * Команда будет передана контроллеру, который решает, как её обработать.
 */
public interface ControlCommand {
    void execute(SimulationController controller);
}
