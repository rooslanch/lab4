package com.example.lab4.simulation.model.mapper;

import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.dto.TerrainDTO;

public class TerrainMapper {
    public static TerrainDTO toDTO (Terrain terrain) {
        return new TerrainDTO(
                terrain.getPoints().stream()
                        .map(p -> new TerrainDTO.Point(p.getX(), p.getH()))
                        .toList()
        );
    }
}
