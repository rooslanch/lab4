package com.example.lab4.simulation.controller.observers;

public class SimpleObserverRegistration implements ObserverRegistration {

    private boolean active = true;
    private final Runnable unregisterCallback;

    /**
     * Делегирование логики коллбека другим объектам через Runnable
     * @param unregisterCallback
     */
    public SimpleObserverRegistration(Runnable unregisterCallback) {
        this.unregisterCallback = unregisterCallback;
    }

    @Override
    public void unregister() {
        if (!active) return;
        active = false;
        unregisterCallback.run();
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
