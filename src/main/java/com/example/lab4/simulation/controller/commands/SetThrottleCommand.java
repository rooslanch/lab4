package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.ThrottleChangedEvent;
import com.example.lab4.simulation.model.PhysicsEngine;
import com.example.lab4.simulation.model.PhysicsModel;

/**
 * Устанавливает силу тяги (может быть отрицательной).
 */
public class SetThrottleCommand implements ControlCommand {

    private final double throttleForce;

    public SetThrottleCommand(double throttleForce) {
        this.throttleForce = throttleForce;
    }

    @Override
    public void execute(SimulationController controller) {
        controller.getPhysicsEngine().setThrottleForce(throttleForce);
        controller.dispatchEvent(new ThrottleChangedEvent(throttleForce));
    }

    public double getThrottleForce() {
        return throttleForce;
    }

    @Override
    public String toString() {
        return "SetThrottleCommand{throttle=" + throttleForce + '}';
    }
}
