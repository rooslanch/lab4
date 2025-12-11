package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.MassChangedEvent;
import com.example.lab4.simulation.controller.events.PausedChangedEvent;

public class SetMassCommand implements ControlCommand {
    private final double newMass;

    public SetMassCommand(double newMass) {
        this.newMass = newMass;
    }

    @Override
    public void execute(SimulationController controller) {
        controller.getModel().setMass(newMass);
        controller.dispatchEvent(new MassChangedEvent(newMass));
    }


}

