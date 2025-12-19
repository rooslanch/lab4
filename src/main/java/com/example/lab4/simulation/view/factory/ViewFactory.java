package com.example.lab4.simulation.view.factory;
import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.view.UIStateModel;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.control.ControlWindow;
import com.example.lab4.simulation.view.log.LogObserver;
import com.example.lab4.simulation.view.main.MainView;
import javafx.stage.Stage;

public class ViewFactory {

    private final SimulationController controller;
    private final UIStateModel uiState = new UIStateModel();

    public ViewFactory(SimulationController controller) {
        this.controller = controller;
    }

    public UIStateModel getUIState() {
        return uiState;
    }



    /** Главное окно: кнопки + TerrainCanvas */
    public void createMainWindow(Stage primaryStage) {
        MainView view = new MainView(controller, this, uiState);
        primaryStage.setScene(new javafx.scene.Scene(view.getRoot(), 1200, 800));
        primaryStage.setTitle("Simulation");
        primaryStage.show();
    }

    /** Окно со слайдерами (масса + тяга) */
    public void createControlWindow() {
        ControlWindow window = new ControlWindow(controller);
        uiState.addObserver(window);
        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);
        window.setOnCloseCallback(() -> uiState.removeObserver(window));
        window.show();
    }

    /** Новое окно логов (каждый вызов — новый observer) */
    public void createLogWindow() {
        LogObserver observer = new LogObserver();
        ObserverRegistration reg = controller.addObserver(observer);
        observer.setRegistration(reg);
    }


    /** Новое окно графиков (каждый вызов — новый observer) */
    public void createChartsWindow() {
        ChartsWindow window = new ChartsWindow(controller.getDt());
        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);
        window.show();
    }
}
