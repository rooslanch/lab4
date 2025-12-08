package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.model.PresetTerrainFactory;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) {

        // ---------------- Террейн ----------------
        Terrain terrain = PresetTerrainFactory.createWave(50,2,2);
        PhysicsModel model = new PhysicsModel(1000); // масса 1000 кг

        // ---------------- Контроллер ----------------
        SimulationController controller = new SimulationControllerImpl(terrain, model);

        // ---------------- Графики ----------------
        ChartsWindow chartsWindow = new ChartsWindow(controller.getDt());
        controller.addObserver(chartsWindow);
        chartsWindow.show();

        // ---------------- GUI ----------------
        MainView mainView = new MainView(controller);
        BorderPane root = mainView.getRoot();

        // ---------------- Сцена ----------------
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Simulation Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
