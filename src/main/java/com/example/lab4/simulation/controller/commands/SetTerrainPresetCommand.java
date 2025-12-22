package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.TerrainChangedEvent;
import com.example.lab4.simulation.model.PresetTerrainFactory;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.TerrainPreset;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.model.mapper.FrictionMapper;
import com.example.lab4.simulation.model.mapper.TerrainMapper;

public class SetTerrainPresetCommand implements ControlCommand {

    private final TerrainPreset preset;

    public SetTerrainPresetCommand(TerrainPreset preset) {
        this.preset = preset;
    }

    @Override
    public void execute(SimulationController controller) {

        // 1. стоп симуляции
        controller.stop();

        // 2. создаём новый террейн
        Terrain terrain = PresetTerrainFactory.create(preset);

        // 3. reset модели
        controller.getModel().reset();
        controller.getPhysicsEngine().setThrottleForce(0);
        controller.setTerrain(terrain);
        TerrainDTO terrainDTO = TerrainMapper.toDTO(terrain);
        FrictionDTO frictionDTO = FrictionMapper.toDTO(terrain.getFrictionProfile());
        // 4. событие
        controller.dispatchEvent(new TerrainChangedEvent(terrainDTO, frictionDTO));
    }
}
