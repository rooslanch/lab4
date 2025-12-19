package com.example.lab4.simulation.view;

import java.util.ArrayList;
import java.util.List;

public class UIStateModel {

    private SimulationUIState state = SimulationUIState.STOPPED;
    private final List<UIStateObserver> observers = new ArrayList<>();

    public void addObserver(UIStateObserver o) {
        observers.add(o);
    }

    public void removeObserver(UIStateObserver o) {
        observers.remove(o);
    }

    public SimulationUIState getState() {
        return state;
    }

    public void setState(SimulationUIState newState) {
        if (state == newState) return;
        state = newState;
        observers.forEach(o -> o.onUIStateChanged(state));
    }
}
