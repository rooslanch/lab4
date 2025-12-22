package com.example.lab4.simulation.view.factory;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.view.UIStateModel;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.control.ControlWindow;
import com.example.lab4.simulation.view.log.LogWindow;
import com.example.lab4.simulation.view.main.MainView;
import com.example.lab4.simulation.view.main.MainViewController;
import javafx.stage.Stage;

public class ViewFactory {

    private final SimulationController controller;
    private final UIStateModel uiState = new UIStateModel();
    private final MainViewController viewController;

    public ViewFactory(SimulationController controller, MainViewController viewController) {
        this.controller = controller;
        this.viewController = viewController;
    }

    public UIStateModel getUIState() {
        return uiState;
    }

    // Возвращаем главный view
    public MainView createMainView(Stage primaryStage) {
        MainView view = new MainView(controller, uiState, viewController);
        primaryStage.setScene(new javafx.scene.Scene(view.getRoot(), 1200, 800));
        primaryStage.setTitle("Simulation");
        primaryStage.show();
        return view;
    }

    public ControlWindow createControlWindow() {
        return new ControlWindow(controller);
    }

    public ChartsWindow createChartsWindow() {
        return new ChartsWindow(controller.getDt());
    }

    public LogWindow createLogWindow() {
        return new LogWindow();
    }
}
