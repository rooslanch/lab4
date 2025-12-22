package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.io.excel.ExcelExporterObserver;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.PresetTerrainFactory;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.view.factory.ViewFactory;
import com.example.lab4.simulation.view.main.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Terrain terrain = PresetTerrainFactory.createWave(50, 2, 2);
        PhysicsModel model = new PhysicsModel(1);
        SimulationController controller = new SimulationControllerImpl(terrain, model);

        String exportDir = "C:\\Users\\Руслан\\Desktop\\simulation_lab4";
        ExcelExporterObserver excelObserver = new ExcelExporterObserver(new File(exportDir));
        controller.addObserver(excelObserver);


        MainViewController mainController = new MainViewController(controller);
        ViewFactory factory = new ViewFactory(controller, mainController);
        mainController.setFactory(factory);

        mainController.initMainView(primaryStage);

        mainController.showControlWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}
