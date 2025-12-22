package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.PausedChangedEvent;
import com.example.lab4.simulation.controller.thread.SimulationLoop;


public class SetPausedCommand implements ControlCommand {

    private final boolean paused;

    public SetPausedCommand(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void execute(SimulationController controller) {
        SimulationLoop loop = controller.getSimulationLoop();
        if (loop != null) {
            loop.setPaused(paused);
        }
        controller.dispatchEvent(new PausedChangedEvent(paused));
    }


    public boolean isPaused() {
        return paused;
    }

    @Override
    public String toString() {
        return "SetPausedCommand{paused=" + paused + '}';
    }
}
