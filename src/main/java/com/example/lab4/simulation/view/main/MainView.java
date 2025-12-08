package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.ResetSimulationCommand;
import com.example.lab4.simulation.controller.commands.SetPausedCommand;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root;
    private final TerrainCanvas terrainCanvas;
    private final SimulationController controller;

    public MainView(SimulationController controller) {
        this.controller = controller;
        this.root = new BorderPane();

        // Получаем DTO один раз
        TerrainDTO terrainDTO = controller.getTerrainDTO();

        // Canvas для рисования террейна и объекта
        terrainCanvas = new TerrainCanvas(controller, terrainDTO);
        root.setCenter(terrainCanvas);

        // Привязываем размеры канваса к центру BorderPane
        terrainCanvas.widthProperty().bind(root.widthProperty().subtract(250)); // 250px для панели слева
        terrainCanvas.heightProperty().bind(root.heightProperty());

        // ---------------- Панель управления ----------------
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(10));

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

        Label throttleLabel = new Label("Throttle Force");
        Slider throttleSlider = new Slider(-5000, 5000, 0);
        throttleSlider.setMajorTickUnit(1000);  // Крупные деления каждые 1000
        throttleSlider.setMinorTickCount(4);     // Мелкие деления между крупными (5 частей)
        throttleSlider.setShowTickLabels(true);
        throttleSlider.setShowTickMarks(true);
        throttleSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                controller.execute(new SetThrottleCommand(newVal.doubleValue()))
        );

        leftPanel.getChildren().addAll(buttons, throttleLabel, throttleSlider);

        root.setLeft(leftPanel);
    }

    public BorderPane getRoot() {
        return root;
    }
}
