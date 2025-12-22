package com.example.lab4.simulation.io;

import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.io.dto.TerrainFileDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TerrainIO {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void save(File file, Terrain terrain) throws IOException {
        TerrainFileDTO dto = TerrainMapper.toJson(terrain);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, dto);
    }

    public static Terrain load(File file) throws IOException {
        TerrainFileDTO dto = mapper.readValue(file, TerrainFileDTO.class);
        return TerrainMapper.fromJson(dto);
    }
}
