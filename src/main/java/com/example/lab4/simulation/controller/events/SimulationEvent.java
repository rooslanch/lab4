package com.example.lab4.simulation.controller.events;

/**
 * Маркерный интерфейс для всех событий симуляции.
 */
public interface SimulationEvent {
    long getTimestamp();   // время генерации события
}