package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.events.TerrainChangedEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainCanvas extends Canvas implements SimulationObserver {

    private TerrainDTO terrainDTO;
    private FrictionDTO frictionDTO;
    private final SimulationController controller;

    private double objectX = 0; // текущая позиция объекта

    public TerrainCanvas(SimulationController controller, TerrainDTO dto, FrictionDTO frictionDTO) {
        this.controller = controller;
        this.terrainDTO = dto;
        this.frictionDTO = frictionDTO;

        controller.addObserver(this);

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void setTerrainDTO(TerrainDTO dto) {
        this.terrainDTO = dto;
        draw();
    }

    public void setFrictionDTO(FrictionDTO dto) {
        this.frictionDTO = dto;
        draw();
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof StateUpdateEvent e) {
            objectX = e.getX();
            Platform.runLater(this::draw);
        }
        if (ev instanceof TerrainChangedEvent e) {
            this.terrainDTO = e.getTerrainDTO();
            this.frictionDTO = e.getFrictionDTO();
            this.objectX = 0;
            Platform.runLater(this::draw);
        }
    }

    public void draw() {
        double width = getWidth();
        double height = getHeight();

        if (width <= 0 || height <= 0) return;
        if (terrainDTO == null) return;

        var points = terrainDTO.getPoints();
        if (points.size() < 2) return;

        double minX = points.stream().mapToDouble(p -> p.getX()).min().orElse(0);
        double maxX = points.stream().mapToDouble(p -> p.getX()).max().orElse(1);
        double minH = points.stream().mapToDouble(p -> p.getH()).min().orElse(0);
        double maxH = points.stream().mapToDouble(p -> p.getH()).max().orElse(1);
        if (minH == maxH) {
            maxH = minH + 10;
            minH = minH - 5;
        }

        double padding = 10;
        double rightMarginFactor = 0.1;
        double terrainWidth = maxX - minX;
        double extendedMaxX = maxX + terrainWidth * rightMarginFactor;
        double scaleX = width / (extendedMaxX - minX);
        double scaleY = (height - 2 * padding) / (maxH - minH);

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);



        // --- Рисуем террейн ---
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        for (int i = 0; i < points.size() - 1; i++) {

            double xWorld1 = points.get(i).getX();
            double xWorld2 = points.get(i + 1).getX();

            double x1 = (xWorld1 - minX) * scaleX;
            double y1 = height - padding - (points.get(i).getH() - minH) * scaleY;

            double x2 = (xWorld2 - minX) * scaleX;
            double y2 = height - padding - (points.get(i + 1).getH() - minH) * scaleY;

            // --- трение для сегмента ---
            double friction = 0.0;
            if (frictionDTO != null) {
                double midX = (xWorld1 + xWorld2) / 2.0;
                friction = frictionDTO.getFrictionAt(midX);
            }

            // нормализуем (0..1)
            double t = Math.min(Math.max(friction, 0.0), 1.0);

            Color color = Color.LIME.interpolate(Color.RED, t);

            gc.setStroke(color);

            gc.strokeLine(x1, y1, x2, y2);
        }

        // --- Рисуем точки террейна ---
        gc.setFill(Color.RED);
        double pointRadius = 4;
        for (var p : points) {
            double x = (p.getX() - minX) * scaleX;
            double y = height - padding - (p.getH() - minH) * scaleY;
            gc.fillOval(x - pointRadius / 2, y - pointRadius / 2, pointRadius, pointRadius);
        }

        // --- Рисуем машину ---
        gc.setFill(Color.BLUE);
        double carX = (objectX - minX) * scaleX;
        double carY = height - padding - (controller.getTerrain().getHeightAt(objectX) - minH) * scaleY;
        double carRadius = 10;
        gc.fillOval(carX - carRadius / 2, carY - carRadius / 2, carRadius, carRadius);
    }
}
