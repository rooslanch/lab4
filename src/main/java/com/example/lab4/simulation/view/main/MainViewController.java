package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.view.AbstractWindow;
import com.example.lab4.simulation.view.UIStateModel;
import com.example.lab4.simulation.view.factory.ViewFactory;
import com.example.lab4.simulation.view.charts.ChartsWindow;
import com.example.lab4.simulation.view.control.ControlWindow;
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

        // При закрытии главного окна закрываем все дочерние
        primaryStage.setOnCloseRequest(e -> closeAllChildWindows());
    }

    public void showControlWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        ControlWindow window = factory.createControlWindow();

        // Подписка на UIState
        uiState.addObserver(window);

        // Подписка на Controller
        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);

        // OnClose callback
        window.setOnCloseCallback(() -> uiState.removeObserver(window));

        childWindows.add(window);
        window.show();
    }

    public void showChartsWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        ChartsWindow window = factory.createChartsWindow();

        ObserverRegistration reg = controller.addObserver(window);
        window.setRegistration(reg);

        // Можно сделать callback через childWindows или отдельный логический callback
        window.setOnCloseCallback(() -> {}); // при необходимости

        childWindows.add(window);
        window.show();
    }

    public void showLogWindow() {
        if (factory == null)
            throw new IllegalStateException("Factory not set! Call setFactory() before using MainViewController.");
        // фабрика создаёт новый LogWindow, который уже наследует AbstractWindow
        LogWindow logWindow = factory.createLogWindow(); // возвращает LogWindow, не void

        // подписываем на контроллер
        ObserverRegistration reg = controller.addObserver(logWindow);
        logWindow.setRegistration(reg);

        // добавляем callback при закрытии для корректной отписки и удаления из списка дочерних окон
        logWindow.setOnCloseCallback(() -> {
            childWindows.remove(logWindow);
            reg.unregister();
        });

        // добавляем в список дочерних окон
        childWindows.add(logWindow);

        // показываем окно
        logWindow.show();
    }


    private void closeAllChildWindows() {
        System.out.println("[MainViewController] Closing all child windows");
        for (AbstractWindow w : childWindows) {
            w.close();
        }

        childWindows.clear();
    }
}
