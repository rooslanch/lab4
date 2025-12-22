package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.*;
import com.example.lab4.simulation.model.TerrainPreset;
import com.example.lab4.simulation.view.SimulationUIState;
import com.example.lab4.simulation.view.UIStateModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class MainControlsPanel {

    private final SimulationController controller;
    private final UIStateModel uiState;
    private final MainViewController mainController;
    public MainControlsPanel(SimulationController controller,
                             UIStateModel uiState,
                             MainViewController mainController) {
        this.controller = controller;
        this.uiState = uiState;
        this.mainController = mainController;
    }

    public VBox create() {
        VBox panel = new VBox(10);
        VBox simButtons = new VBox(10);

        // ---- Simulation buttons ----
        HBox simRow1 = new HBox(10);
        HBox simRow2 = new HBox(10);
        HBox presetRow = new HBox(10);

        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button pauseBtn = new Button("Pause");
        Button resumeBtn = new Button("Resume");
        Button resetBtn = new Button("Reset");
        Button loadBtn = new Button("Load terrain");
        Button saveBtn = new Button("Save terrain");
        Label presetLabel = new Label("Terrain preset:");
        ComboBox<TerrainPreset> presetBox = new ComboBox<>();
        presetBox.getItems().addAll(TerrainPreset.values());
        presetBox.setValue(TerrainPreset.WAVE); // дефолт

        presetBox.setOnAction(e -> {
            controller.execute(new SetTerrainPresetCommand(presetBox.getValue()));
            uiState.setState(SimulationUIState.STOPPED);
        });
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



        loadBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Terrain JSON", "*.json")
            );
            File file = fc.showOpenDialog(null);
            if (file != null) {
                controller.execute(new LoadTerrainCommand(file));
                uiState.setState(SimulationUIState.STOPPED);
            }

        });

        saveBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Terrain JSON", "*.json")
            );
            File file = fc.showSaveDialog(null);
            if (file != null) {
                controller.execute(new SaveTerrainCommand(file));
            }
        });


        presetRow.setAlignment(Pos.CENTER_LEFT);
        presetRow.getChildren().addAll(presetLabel, presetBox);
        simRow1.getChildren().addAll(startBtn, stopBtn, pauseBtn, resumeBtn);
        simRow2.getChildren().addAll(resetBtn, loadBtn, saveBtn);
        simButtons.getChildren().addAll(simRow1, simRow2,presetRow);
        // ---- View buttons ----
        HBox viewButtons = new HBox(10);

        Button showLogBtn = new Button("Show log");
        Button showChartsBtn = new Button("Show charts");

        // делегируем создание окон главному контроллеру вида
        showLogBtn.setOnAction(e -> mainController.showLogWindow());
        showChartsBtn.setOnAction(e -> mainController.showChartsWindow());

        viewButtons.getChildren().addAll(showLogBtn, showChartsBtn);

        panel.getChildren().addAll(simButtons, viewButtons);

        return panel;
    }
}
