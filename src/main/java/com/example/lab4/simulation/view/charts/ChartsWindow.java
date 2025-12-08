package com.example.lab4.simulation.view.charts;

import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ChartsWindow implements SimulationObserver {
    private static final int WINDOW_SIZE = 500;
    private final Stage stage;
    private final NumberAxis speedXAxis = new NumberAxis();
    private final NumberAxis positionXAxis = new NumberAxis();
    private final XYChart.Series<Number, Number> speedSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> positionSeries = new XYChart.Series<>();
    private double time = 0;
    private final double dt;

    public ChartsWindow(double dt) {
        this.dt = dt;
        stage = new Stage();
        stage.setTitle("Simulation Charts");

        speedXAxis.setAutoRanging(false);
        positionXAxis.setAutoRanging(false);

        double window = WINDOW_SIZE * dt;
        speedXAxis.setLowerBound(0);
        speedXAxis.setUpperBound(window);
        positionXAxis.setLowerBound(0);
        positionXAxis.setUpperBound(window);

        NumberAxis speedYAxis = new NumberAxis();
        speedYAxis.setAutoRanging(true);
        speedXAxis.setLabel("Time, s");
        speedYAxis.setLabel("Speed, m/s");
        LineChart<Number, Number> speedChart = new LineChart<>(speedXAxis, speedYAxis);
        speedChart.setAnimated(false);
        speedSeries.setName("v(t)");
        speedChart.getData().add(speedSeries);

        NumberAxis posYAxis = new NumberAxis();
        posYAxis.setAutoRanging(true);
        positionXAxis.setLabel("Time, s");
        posYAxis.setLabel("Position, m");
        LineChart<Number, Number> posChart = new LineChart<>(positionXAxis, posYAxis);
        posChart.setAnimated(false);
        positionSeries.setName("x(t)");
        posChart.getData().add(positionSeries);

        var root = new javafx.scene.layout.VBox(speedChart, posChart);
        root.setSpacing(10);
        stage.setScene(new Scene(root, 800, 600));
    }

    public void show() {
        stage.show();
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (!(ev instanceof StateUpdateEvent e)) return;

        time += dt;

        Platform.runLater(() -> {
            speedSeries.getData().add(new XYChart.Data<>(time, e.getV()));
            positionSeries.getData().add(new XYChart.Data<>(time, e.getX()));

            // Удаляем старые точки, если превышен лимит
            if (speedSeries.getData().size() > WINDOW_SIZE) {
                speedSeries.getData().remove(0);
            }
            if (positionSeries.getData().size() > WINDOW_SIZE) {
                positionSeries.getData().remove(0);
            }

            double window = WINDOW_SIZE * dt;
            double minX = time - window;

            if (minX < 0) {
                minX = 0;
                // Пока данные не заполнили окно, увеличиваем правую границу
                speedXAxis.setUpperBound(time);
                positionXAxis.setUpperBound(time);
            } else {
                // Когда окно заполнено, двигаем его
                speedXAxis.setLowerBound(minX);
                speedXAxis.setUpperBound(time);
                positionXAxis.setLowerBound(minX);
                positionXAxis.setUpperBound(time);
            }
        });
    }

    public void reset() {
        Platform.runLater(() -> {
            speedSeries.getData().clear();
            positionSeries.getData().clear();
            time = 0;

            // Сбрасываем границы осей
            double window = WINDOW_SIZE * dt;
            speedXAxis.setLowerBound(0);
            speedXAxis.setUpperBound(window);
            positionXAxis.setLowerBound(0);
            positionXAxis.setUpperBound(window);
        });
    }
}