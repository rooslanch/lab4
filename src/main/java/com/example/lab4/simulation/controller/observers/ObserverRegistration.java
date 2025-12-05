package com.example.lab4.simulation.controller.observers;

/**
 * handle для отписки
 *  */
public interface ObserverRegistration {

    /** Отписывает наблюдателя от контроллера */
    void unregister();

    /** Флаг, что подписчик уже отписан */
    boolean isActive();
}
