package com.example.lab4.simulation;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.SimulationControllerImpl;
import com.example.lab4.simulation.model.PresetTerrainFactory;
import com.example.lab4.simulation.model.PhysicsModel;
import com.example.lab4.simulation.model.Terrain;
import com.example.lab4.simulation.persistence.excel.ExcelExporterObserver;
import com.example.lab4.simulation.view.factory.ViewFactory;
import com.example.lab4.simulation.view.main.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ---- Модель и контроллер симуляции ----
        Terrain terrain = PresetTerrainFactory.createWave(50, 2, 2);
        PhysicsModel model = new PhysicsModel(1); // масса 1 кг для примера
        SimulationController controller = new SimulationControllerImpl(terrain, model);

        // ---- Экспорт в Excel ----
        String exportDir = "C:\\Users\\Руслан\\Desktop\\simulation_lab4";
        ExcelExporterObserver excelObserver = new ExcelExporterObserver(new File(exportDir));
        controller.addObserver(excelObserver);




        // ---- Главный контроллер вида ----
        MainViewController mainController = new MainViewController(controller);
        // ---- Фабрика окон ----
        ViewFactory factory = new ViewFactory(controller, mainController);
        mainController.setFactory(factory);

        // создаем и показываем главное окно
        mainController.initMainView(primaryStage);

        // автоматически создаем окно управления (слайдеры)
        mainController.showControlWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}
