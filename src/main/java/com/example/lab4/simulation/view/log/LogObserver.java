package com.example.lab4.simulation.view.log;

import com.example.lab4.simulation.controller.events.*;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogObserver implements SimulationObserver {

    private final LogWindow window = new LogWindow();
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    private void log(String msg) {
        window.enqueue("[" + sdf.format(new Date()) + "] " + msg);
    }

    @Override
    public void onEvent(SimulationEvent ev) {

        if (ev instanceof StateUpdateEvent s) {
            log(String.format(
                    "State: x=%.2f, v=%.2f, a=%.2f, slope=%.2f",
                    s.getX(), s.getV(), s.getA(), s.getSlope()
            ));
        }

        if (ev instanceof BoundaryReachedEvent b) {
            log("Boundary reached: " + b.getType() + " at x=" + b.getX());
        }

        if (ev instanceof RollingBackEvent r) {
            log(String.format("Rolling back: x=%.2f, v=%.2f, slope=%.2f",
                    r.getX(), r.getV(), r.getSlope()));
        }

        if (ev instanceof SimulationResetEvent) {
            log("Simulation reset");
        }

        if (ev instanceof ThrottleChangedEvent t) {
            log(String.format("Throttle changed: %.2f",
                    t.getNewThrottleForce()));
        }

        if (ev instanceof PositionChangedEvent p) {
            log(String.format("Position changed: x=%.2f", p.getX()));
        }

        if (ev instanceof MassChangedEvent m) {
            log(String.format("Mass changed: x=%.2f", m.getNewMass()));
        }

        if (ev instanceof FrictionChangedEvent f) {
            log(String.format("Friction changed: x=%.2f", f.getNewFriction()));
        }

    }
}

