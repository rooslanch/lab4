package com.example.lab4.simulation.view.charts;

import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.view.AbstractWindow;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChartsWindow extends AbstractWindow implements SimulationObserver {

    private static final int WINDOW_SIZE = 500;


    private ObserverRegistration registration;

    private final NumberAxis speedXAxis = new NumberAxis();
    private final NumberAxis positionXAxis = new NumberAxis();
    private final XYChart.Series<Number, Number> speedSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> positionSeries = new XYChart.Series<>();

    private double time = 0;
    private final double dt;

    private final Button toggleBtn = new Button("Register");

    public ChartsWindow(double dt) {
        this.dt = dt;
        stage = new Stage();
        stage.setTitle("Simulation Charts");

        initAxes();
        VBox root = new VBox(10, toggleBtn, createSpeedChart(), createPositionChart());
        stage.setScene(new Scene(root, 800, 650));

        toggleBtn.setOnAction(e -> toggleRegistration());

        stage.setOnCloseRequest(e -> {
            handleClose();
        });
    }

    private void initAxes() {
        speedXAxis.setAutoRanging(false);
        positionXAxis.setAutoRanging(false);

        double window = WINDOW_SIZE * dt;
        speedXAxis.setLowerBound(0);
        speedXAxis.setUpperBound(window);
        positionXAxis.setLowerBound(0);
        positionXAxis.setUpperBound(window);
    }

    private LineChart<Number, Number> createSpeedChart() {
        NumberAxis y = new NumberAxis();
        y.setAutoRanging(true);
        speedXAxis.setLabel("Time, s");
        y.setLabel("Speed, m/s");

        LineChart<Number, Number> chart = new LineChart<>(speedXAxis, y);
        chart.setAnimated(false);
        speedSeries.setName("v(t)");
        chart.getData().add(speedSeries);
        return chart;
    }

    private LineChart<Number, Number> createPositionChart() {
        NumberAxis y = new NumberAxis();
        y.setAutoRanging(true);
        positionXAxis.setLabel("Time, s");
        y.setLabel("Position, m");

        LineChart<Number, Number> chart = new LineChart<>(positionXAxis, y);
        chart.setAnimated(false);
        positionSeries.setName("x(t)");
        chart.getData().add(positionSeries);
        return chart;
    }

    public void setRegistration(ObserverRegistration registration) {
        this.registration = registration;
        updateButton();
    }

    private void toggleRegistration() {
        if (registration == null || !registration.isActive()) return;
        registration.unregister();
        updateButton();
    }

    private void updateButton() {
        toggleBtn.setText(
                registration != null && registration.isActive()
                        ? "Unregister"
                        : "Register"
        );
    }

    public void show() {
        stage.show();
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (registration == null || !registration.isActive()) return;
        if (!(ev instanceof StateUpdateEvent e)) return;

        time += dt;

        Platform.runLater(() -> {
            speedSeries.getData().add(new XYChart.Data<>(time, e.getV()));
            positionSeries.getData().add(new XYChart.Data<>(time, e.getX()));

            trimSeries(speedSeries);
            trimSeries(positionSeries);
            updateAxisWindow();
        });
    }

    private void trimSeries(XYChart.Series<Number, Number> s) {
        if (s.getData().size() > WINDOW_SIZE) {
            s.getData().remove(0);
        }
    }

    private void updateAxisWindow() {
        double window = WINDOW_SIZE * dt;
        double minX = time - window;

        if (minX < 0) {
            speedXAxis.setUpperBound(time);
            positionXAxis.setUpperBound(time);
        } else {
            speedXAxis.setLowerBound(minX);
            speedXAxis.setUpperBound(time);
            positionXAxis.setLowerBound(minX);
            positionXAxis.setUpperBound(time);
        }
    }

    /** Закрытие окна */
    public void handleClose() {
        if (registration != null) registration.unregister();
        if (onCloseCallback != null) onCloseCallback.run();
        if (onClosedByItself != null) onClosedByItself.run();
    }

}
