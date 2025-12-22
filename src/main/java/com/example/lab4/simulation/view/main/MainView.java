package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.model.dto.FrictionDTO;
import com.example.lab4.simulation.model.dto.TerrainDTO;
import com.example.lab4.simulation.view.UIStateModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root = new BorderPane();

    public MainView(SimulationController controller, UIStateModel uiStateModel, MainViewController viewController) {

        // --- Terrain ---
        TerrainDTO terrainDTO = controller.getTerrainDTO();
        FrictionDTO frictionDTO = controller.getFrictionDTO();

        TerrainCanvas canvas = new TerrainCanvas(controller, terrainDTO, frictionDTO);
        root.setCenter(canvas);

        canvas.widthProperty().bind(root.widthProperty().subtract(250));
        canvas.heightProperty().bind(root.heightProperty());

        // --- Controls ---
        MainControlsPanel controls = new MainControlsPanel(controller, uiStateModel, viewController);
        VBox leftPanel = controls.create();

        root.setLeft(leftPanel);
    }

    public BorderPane getRoot() {
        return root;
    }
}
