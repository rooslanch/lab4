package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.ResetSimulationCommand;
import com.example.lab4.simulation.controller.commands.SetPausedCommand;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import com.example.lab4.simulation.model.dto.FrictionDTO;
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

    public MainView(SimulationController controller) {
        this.root = new BorderPane();

        // DTO террейна
        TerrainDTO terrainDTO = controller.getTerrainDTO();
        FrictionDTO frictionDTO = controller.getFrictionDTO();

        // Канвас
        terrainCanvas = new TerrainCanvas(controller, terrainDTO, frictionDTO);
        root.setCenter(terrainCanvas);

        terrainCanvas.widthProperty().bind(root.widthProperty().subtract(250));
        terrainCanvas.heightProperty().bind(root.heightProperty());

        // ---- Контроллер View ----
        MainViewController viewController = new MainViewController(controller);

        // Добавляем панель
        VBox leftPanel = viewController.createControlPanel();
        root.setLeft(leftPanel);
    }

    public BorderPane getRoot() {
        return root;
    }
}

