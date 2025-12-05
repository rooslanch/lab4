package com.example.lab4.simulation.controller.observers;

import com.example.lab4.simulation.controller.events.*;

public interface SimulationObserver {

    /**
     * Получить событие из контроллера.
     */
    void onEvent(SimulationEvent event);

}
