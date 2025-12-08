package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainCanvas extends Canvas implements SimulationObserver {

    private TerrainDTO terrainDTO;
    private final SimulationController controller;

    private double objectX = 0; // текущая позиция объекта

    public TerrainCanvas(SimulationController controller, TerrainDTO dto) {
        this.controller = controller;
        this.terrainDTO = dto;

        // Подписка на события модели
        controller.addObserver(this);

        // Рисуем при изменении размеров
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void setTerrainDTO(TerrainDTO dto) {
        this.terrainDTO = dto;
        draw();
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof StateUpdateEvent e) {
            objectX = e.getX(); // обновляем позицию машины
            Platform.runLater(this::draw); // перерисовка на FX-потоке
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

        double padding = 10;
        double rightMarginFactor = 0.1;
        double terrainWidth = maxX - minX;
        double extendedMaxX = maxX + terrainWidth * rightMarginFactor;
        double scaleX = (extendedMaxX - minX == 0) ? 1 : width / (extendedMaxX - minX);
        double scaleY;
        if (maxH - minH == 0) {
            scaleY = height / 2;
            minH -= 0.5;
            maxH += 0.5;
        } else {
            scaleY = (height - 2 * padding) / (maxH - minH);
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // --- Рисуем террейн ---
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (int i = 0; i < points.size() - 1; i++) {
            double x1 = (points.get(i).getX() - minX) * scaleX;
            double y1 = height - padding - (points.get(i).getH() - minH) * scaleY;
            double x2 = (points.get(i + 1).getX() - minX) * scaleX;
            double y2 = height - padding - (points.get(i + 1).getH() - minH) * scaleY;
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
