package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.ResetSimulationCommand;
import com.example.lab4.simulation.controller.commands.SetPausedCommand;
import com.example.lab4.simulation.view.SimulationUIState;
import com.example.lab4.simulation.view.UIStateModel;
import com.example.lab4.simulation.view.factory.ViewFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainControlsPanel {

    private final SimulationController controller;
    private final ViewFactory viewFactory;
    private final UIStateModel uiState;
    public MainControlsPanel(SimulationController controller,
                             ViewFactory viewFactory,
                             UIStateModel uiState) {
        this.controller = controller;
        this.viewFactory = viewFactory;
        this.uiState = uiState;
    }

    public VBox create() {
        VBox panel = new VBox(15);

        // ---- Simulation buttons ----
        HBox simButtons = new HBox(10);

        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button pauseBtn = new Button("Pause");
        Button resumeBtn = new Button("Resume");
        Button resetBtn = new Button("Reset");

        startBtn.setOnAction(e -> {
            controller.start();
            uiState.setState(SimulationUIState.RUNNING);
        });
        stopBtn.setOnAction(e -> {
            controller.stop();
            uiState.setState(SimulationUIState.STOPPED);
        });
        pauseBtn.setOnAction(e -> {
            controller.execute(new SetPausedCommand(true));
            uiState.setState(SimulationUIState.PAUSED);
        });
        resumeBtn.setOnAction(e -> {
            controller.execute(new SetPausedCommand(false));
            uiState.setState(SimulationUIState.RUNNING);
        });
        resetBtn.setOnAction(e -> {
            controller.execute(new ResetSimulationCommand());
            uiState.setState(SimulationUIState.STOPPED);
        });

        simButtons.getChildren().addAll(
                startBtn, stopBtn, pauseBtn, resumeBtn, resetBtn
        );

        // ---- View buttons ----
        HBox viewButtons = new HBox(10);

        Button showLogBtn = new Button("Show log");
        Button showChartsBtn = new Button("Show charts");

        showLogBtn.setOnAction(e -> viewFactory.createLogWindow());
        showChartsBtn.setOnAction(e -> viewFactory.createChartsWindow());

        viewButtons.getChildren().addAll(showLogBtn, showChartsBtn);

        panel.getChildren().addAll(simButtons, viewButtons);

        return panel;
    }
}
