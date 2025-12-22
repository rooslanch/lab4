package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.GravityChangedEvent;

public class SetGravityCommand implements ControlCommand {

    private final double newGravity;

    public SetGravityCommand(double newGravity) {
        this.newGravity = newGravity;
    }

    @Override
    public void execute(SimulationController controller) {
        controller.getPhysicsEngine().setGravity(newGravity);
        controller.dispatchEvent(new GravityChangedEvent(newGravity));
    }
}
