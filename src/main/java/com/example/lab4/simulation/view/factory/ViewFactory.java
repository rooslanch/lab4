package com.example.lab4.simulation.view.factory;
import com.example.lab4.simulation.controller.SimulationController;
import javafx.stage.Stage;

public class ViewFactory {

    private final SimulationController controller;

    public ViewFactory(SimulationController controller) {
        this.controller = controller;
    }

    /** Главное окно: кнопки + TerrainCanvas */
    public void createMainWindow(Stage primaryStage) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Окно со слайдерами (масса + тяга) */
    public void createControlWindow() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Новое окно логов (каждый вызов — новый observer) */
    public void createLogWindow() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Новое окно графиков (каждый вызов — новый observer) */
    public void createChartsWindow() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
