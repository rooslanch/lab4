package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.model.PresetTerrainFactory;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.excel.ExcelExporterObserver;
import com.example.lab4.simulation.view.log.LogObserver;
import com.example.lab4.simulation.view.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) {

        // ---------------- Террейн ----------------
        Terrain terrain = PresetTerrainFactory.createWave(50,2,2);
        PhysicsModel model = new PhysicsModel(1); // масса 1000 кг

        // ---------------- Контроллер ----------------
        SimulationController controller = new SimulationControllerImpl(terrain, model);

        // ---------------- Графики ----------------
        ChartsWindow chartsWindow = new ChartsWindow(controller.getDt());
        controller.addObserver(chartsWindow);
        chartsWindow.show();

        LogObserver logObserver = new LogObserver();
        controller.addObserver(logObserver);


        // путь к папке для сохранения
        String exportDir = "C:\\Users\\Руслан\\Desktop\\simulation_lab4";

// создаём наблюдателя
        ExcelExporterObserver excelObserver = new ExcelExporterObserver(new File(exportDir));

// подписываемся на контроллер
        controller.addObserver(excelObserver);

        // ---------------- GUI ----------------
        MainView mainView = new MainView(controller); // почему-то не addObserver
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
