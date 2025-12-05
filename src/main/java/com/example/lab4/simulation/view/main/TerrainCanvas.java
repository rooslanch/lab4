package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.model.TerrainPoint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainCanvas extends Canvas implements SimulationObserver {

    private final SimulationController controller;
    private double objectX = 0;
    private double objectY = 0;

    public TerrainCanvas(SimulationController controller) {
        super(800, 500);
        this.controller = controller;

        // Подписываемся на события
        controller.addObserver(this);

        // Рисуем сразу
        draw();
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Рисуем террейн
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        var points = controller.getTerrain().getPoints();
        for (int i = 0; i < points.size() - 1; i++) {
            TerrainPoint p0 = points.get(i);
            TerrainPoint p1 = points.get(i + 1);

            double x0 = mapX(p0.getX());
            double y0 = mapY(p0.getH());
            double x1 = mapX(p1.getX());
            double y1 = mapY(p1.getH());

            gc.strokeLine(x0, y0, x1, y1);
        }

        // Рисуем объект
        gc.setFill(Color.RED);
        gc.fillOval(mapX(objectX) - 5, mapY(objectY) - 5, 10, 10);
    }

    private double mapX(double x) {
        double minX = controller.getTerrain().getMinX();
        double maxX = controller.getTerrain().getMaxX();
        return (x - minX) / (maxX - minX) * getWidth();
    }

    private double mapY(double h) {
        // Инвертируем Y, чтобы 0 было внизу
        double maxH = controller.getTerrain().getPoints().stream().mapToDouble(TerrainPoint::getH).max().orElse(10);
        return getHeight() - (h / maxH) * getHeight();
    }

    @Override
    public void onEvent(com.example.lab4.simulation.controller.events.SimulationEvent ev) {
        if (ev instanceof StateUpdateEvent s) {
            objectX = s.getX();
            objectY = controller.getTerrain().getHeightAt(objectX);

            // Перерисовываем на FX Application Thread
            javafx.application.Platform.runLater(this::draw);
        }
    }
}
