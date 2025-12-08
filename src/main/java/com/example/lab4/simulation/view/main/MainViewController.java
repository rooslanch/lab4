package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.ResetSimulationCommand;
import com.example.lab4.simulation.controller.commands.SetPausedCommand;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainViewController {

    private final SimulationController controller;

    public MainViewController(SimulationController controller) {
        this.controller = controller;
    }

    public VBox createControlPanel() {
        VBox leftPanel = new VBox(20);

        // --------- Кнопки ---------
        HBox buttons = new HBox(10);
        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button pauseBtn = new Button("Pause");
        Button resumeBtn = new Button("Resume");
        Button resetBtn = new Button("Reset");

        startBtn.setOnAction(e -> controller.start());
        stopBtn.setOnAction(e -> controller.stop());
        pauseBtn.setOnAction(e -> controller.execute(new SetPausedCommand(true)));
        resumeBtn.setOnAction(e -> controller.execute(new SetPausedCommand(false)));
        resetBtn.setOnAction(e -> controller.execute(new ResetSimulationCommand()));

        buttons.getChildren().addAll(startBtn, stopBtn, pauseBtn, resumeBtn, resetBtn);

        // --------- Ползунок тяги ---------
        VBox throttleBox = new VBox(5);
        Label label = new Label("Throttle Force");
        Slider throttleSlider = new Slider(-5000, 5000, 0);
        throttleSlider.setShowTickLabels(true);
        throttleSlider.setShowTickMarks(true);
        throttleSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                controller.execute(new SetThrottleCommand(newVal.doubleValue()))
        );

        throttleBox.getChildren().addAll(label, throttleSlider);

        leftPanel.getChildren().addAll(buttons, throttleBox);
        return leftPanel;
    }
}
