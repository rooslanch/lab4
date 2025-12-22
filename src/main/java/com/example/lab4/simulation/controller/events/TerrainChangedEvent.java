package com.example.lab4.simulation.controller.events;

import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.model.dto.FrictionDTO;

public class TerrainChangedEvent implements SimulationEvent {

    private final TerrainDTO terrainDTO;
    private final FrictionDTO frictionDTO;
    private final long timestamp;

    public TerrainChangedEvent(TerrainDTO terrainDTO, FrictionDTO frictionDTO) {
        this.terrainDTO = terrainDTO;
        this.frictionDTO = frictionDTO;
        this.timestamp = System.currentTimeMillis();
    }

    public TerrainDTO getTerrainDTO() {
        return terrainDTO;
    }

    public FrictionDTO getFrictionDTO() {
        return frictionDTO;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TerrainChangedEvent{timestamp=" + timestamp + "}";
    }
}
