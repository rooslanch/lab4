package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.view.AbstractWindow;
import com.example.lab4.simulation.view.UIStateModel;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.control.ControlWindow;
import com.example.lab4.simulation.view.factory.ViewFactory;
import com.example.lab4.simulation.view.log.LogWindow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    private final SimulationController controller;
    private ViewFactory factory;
    private UIStateModel uiState;

    private final List<AbstractWindow> childWindows = new ArrayList<>();
    private MainView mainView;

    public MainViewController(SimulationController controller) {
        this.controller = controller;
    }

    public void setFactory(ViewFactory factory) {
        this.factory = factory;
        this.uiState = factory.getUIState();
    }

    public void initMainView(Stage primaryStage) {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        mainView = factory.createMainView(primaryStage);

        primaryStage.setOnCloseRequest(e -> closeAllChildWindows());
    }

    public void showControlWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        ControlWindow window = factory.createControlWindow();

        uiState.addObserver(window);

        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);

        window.setOnCloseCallback(() -> uiState.removeObserver(window));

        registerChildWindow(window);
        window.show();
    }

    public void showChartsWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        ChartsWindow window = factory.createChartsWindow();

        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);

        registerChildWindow(window);
        window.show();
    }

    public void showLogWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        LogWindow window = factory.createLogWindow();

        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);

        registerChildWindow(window);
        window.show();
    }

    private void registerChildWindow(AbstractWindow window) {
        childWindows.add(window);

        window.setOnClosedByController(() -> {
            System.out.println("[MainViewController] Removing closed window (closed by controller): " + window);
            childWindows.remove(window);
        });
        window.setOnClosedByItself(() -> {
            System.out.println("[MainViewController] Removing closed window (closed by itself): " + window);
            childWindows.remove(window);
        });

    }


    private void closeAllChildWindows() {
        System.out.println("[MainViewController] Closing all child windows");

        List<AbstractWindow> copy = new ArrayList<>(childWindows);

        for (AbstractWindow w : copy) {
            System.out.println("[MainViewController] closing a child window");
            w.close(); // внутри w.close() childWindows будет уменьшаться — и это ОК
        }

        childWindows.clear(); // на всякий случай
    }
}
