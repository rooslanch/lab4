package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.TerrainChangedEvent;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.persistence.TerrainIO;
import com.example.lab4.simulation.model.mapper.TerrainMapper;
import com.example.lab4.simulation.model.mapper.FrictionMapper;

import java.io.File;
import java.io.IOException;

public class LoadTerrainCommand implements ControlCommand {

    private final File file;

    public LoadTerrainCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute(SimulationController controller) {

        // 1. стоп симуляции
        controller.stop();

        // 2. загрузка

        Terrain terrain = null;
        try {
            terrain = TerrainIO.load(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // 3. reset модели
        controller.getModel().reset();
        controller.getPhysicsEngine().setThrottleForce(0);
        controller.setTerrain(terrain);

        // 5. DTO
        TerrainDTO terrainDTO = TerrainMapper.toDTO(terrain);
        FrictionDTO frictionDTO = FrictionMapper.toDTO(terrain.getFrictionProfile());

        // 6. событие
        controller.dispatchEvent(
                new TerrainChangedEvent(terrainDTO, frictionDTO)
        );
    }
}
