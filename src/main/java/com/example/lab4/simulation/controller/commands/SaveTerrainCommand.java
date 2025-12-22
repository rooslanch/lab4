package com.example.lab4.simulation.controller.commands;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.io.TerrainIO;

import java.io.File;
import java.io.IOException;

public class SaveTerrainCommand implements ControlCommand {

    private final File file;

    public SaveTerrainCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute(SimulationController controller) {
        try {
            TerrainIO.save(file, controller.getTerrain());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
