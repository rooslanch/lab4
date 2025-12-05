package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.model.TerrainPoint;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.main.MainView;
import com.example.lab4.simulation.view.charts.PositionChartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) {

        // ------------------------
        // Создаем террейн
        // ------------------------
        List<TerrainPoint> points = List.of(
                new TerrainPoint(0, 0),
                new TerrainPoint(10, 0),
                new TerrainPoint(20, 5),
                new TerrainPoint(30, 0)
        );
        Terrain terrain = new Terrain(points);
        PhysicsModel model = new PhysicsModel(1000); // масса 1000 кг

        // ------------------------
        // Создаем контроллер
        // ------------------------
        SimulationController controller = new SimulationControllerImpl(terrain, model);

        // после создания контроллера
        ChartsWindow chartsWindow = new ChartsWindow(controller.getDt());
        controller.addObserver(chartsWindow);
        chartsWindow.show();

        // ------------------------
        // Создаем GUI
        // ------------------------
        MainView mainView = new MainView(controller);

        // ------------------------
        // Добавляем графики
        // ------------------------


        BorderPane root = mainView.getRoot();


        // ------------------------
        // Старт сцены
        // ------------------------
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Simulation Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
