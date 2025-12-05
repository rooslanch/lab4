package com.example.lab4.view.charts;

import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.view.charts.ChartTimebase;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class SpeedChartView implements SimulationObserver {

    private static final int MAX_POINTS = 300;

    private final LineChart<Number, Number> chart;
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private final NumberAxis xAxis;
    private final NumberAxis yAxis;

    private final ChartTimebase timebase;

    public SpeedChartView(double dt) {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Time, s");
        yAxis.setLabel("Speed, v");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Speed vs Time");
        chart.setAnimated(false);
        chart.getData().add(series);

        this.timebase = new ChartTimebase(dt);
    }

    public LineChart<Number, Number> getNode() {
        return chart;
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        Platform.runLater(() -> {
            if (!(ev instanceof StateUpdateEvent su))
            {
                return;
            }
            double t = timebase.nextTime();
            series.getData().add(new XYChart.Data<>(t, su.getV()));

            if (series.getData().size() > MAX_POINTS) {
                series.getData().remove(0);
            }

            double window = MAX_POINTS * timebase.dt;
            xAxis.setLowerBound(t - window);
            xAxis.setUpperBound(t);
        });
    }
}
