package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.controller.events.*;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.TerrainPoint;

import java.util.List;

public class SimulationTest {

    public static void main(String[] args) throws InterruptedException {

        // 1. Создаём террейн
        Terrain terrain = new Terrain(List.of(
                new TerrainPoint(0, 0),
                new TerrainPoint(10, 0),
                new TerrainPoint(20, 5),
                new TerrainPoint(30, 2)
        ));

        // 2. Создаём модель
        PhysicsModel model = new PhysicsModel(1000); // масса 1000 кг

        // 3. Создаём контроллер
        SimulationControllerImpl controller = new SimulationControllerImpl(terrain, model);

        // 4. Подписываемся на события
        controller.addObserver(new SimulationObserver() {
            @Override
            public void onEvent(SimulationEvent event) {
                System.out.println(event.toString());
            }
        });

        // 5. Запускаем симуляцию
        controller.start();

        // 6. Даём силу тяги
        controller.execute(new SetThrottleCommand(4000)); // 5000 Н

        // 7. Симуляция работает 5 секунд
        Thread.sleep(5000);

        // 8. Останавливаем
        controller.stop();

        System.out.println("Simulation finished. Final position: " + model.getX() +
                ", velocity: " + model.getV());
    }
}
