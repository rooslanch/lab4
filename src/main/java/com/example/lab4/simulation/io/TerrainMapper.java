package com.example.lab4.simulation.io;

import com.example.lab4.simulation.model.*;
import com.example.lab4.simulation.io.dto.*;

import java.util.List;

public class TerrainMapper {


    public static Terrain fromJson(TerrainFileDTO dto) {

        List<TerrainPoint> points = dto.terrain.points.stream()
                .map(p -> new TerrainPoint(p.x, p.h))
                .toList();

        FrictionProfile friction = new FrictionProfile();
        if (dto.friction != null && dto.friction.sections != null) {
            dto.friction.sections.forEach(s ->
                    friction.addSection(s.fromX, s.mu)
            );
        }

        return new Terrain(points, friction);
    }


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
