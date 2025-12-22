package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.TerrainChangedEvent;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.io.TerrainIO;
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

        controller.stop();

        Terrain terrain = null;
        try {
            terrain = TerrainIO.load(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        controller.getModel().reset();
        controller.getPhysicsEngine().setThrottleForce(0);
        controller.setTerrain(terrain);

        TerrainDTO terrainDTO = TerrainMapper.toDTO(terrain);
        FrictionDTO frictionDTO = FrictionMapper.toDTO(terrain.getFrictionProfile());
        controller.dispatchEvent(
                new TerrainChangedEvent(terrainDTO, frictionDTO)
        );
    }
}
