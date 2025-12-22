package com.example.lab4.simulation.persistence;

import com.example.lab4.simulation.model.*;
import com.example.lab4.simulation.persistence.dto.*;

import java.util.List;

public class TerrainMapper {

    /* -------- JSON → MODEL -------- */

    public static Terrain fromJson(TerrainFileDTO dto) {

        // points
        List<TerrainPoint> points = dto.terrain.points.stream()
                .map(p -> new TerrainPoint(p.x, p.h))
                .toList();

        // friction
        FrictionProfile friction = new FrictionProfile();
        if (dto.friction != null && dto.friction.sections != null) {
            dto.friction.sections.forEach(s ->
                    friction.addSection(s.fromX, s.mu)
            );
        }

        return new Terrain(points, friction);
    }

    /* -------- MODEL → JSON -------- */

    public static TerrainFileDTO toJson(Terrain terrain) {

        TerrainFileDTO file = new TerrainFileDTO();

        // terrain
        TerrainJsonDTO terrainDto = new TerrainJsonDTO();
        terrainDto.points = terrain.getPoints().stream().map(p -> {
            TerrainJsonDTO.Point jp = new TerrainJsonDTO.Point();
            jp.x = p.getX();
            jp.h = p.getH();
            return jp;
        }).toList();

        // friction
        FrictionJsonDTO frictionDto = new FrictionJsonDTO();
        frictionDto.sections = terrain.getFrictionProfile()
                .getSections()
                .entrySet()
                .stream()
                .map(e -> {
                    FrictionJsonDTO.Section s = new FrictionJsonDTO.Section();
                    s.fromX = e.getKey();
                    s.mu = e.getValue();
                    return s;
                })
                .toList();

        file.terrain = terrainDto;
        file.friction = frictionDto;

        return file;
    }
}
