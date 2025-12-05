package com.example.lab4.simulation.view.factory;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.view.main.MainView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewFactory {

    private final SimulationController controller;

    public ViewFactory(SimulationController controller) {
        this.controller = controller;
    }

    public void createAndShowMainView() {
        Stage stage = new Stage();
        MainView mainView = new MainView(controller);
        Scene scene = new Scene(mainView.getRoot(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Simulation GUI");
        stage.show();
    }
}
