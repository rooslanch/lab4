package com.example.lab4.simulation.view.control;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.SetGravityCommand;
import com.example.lab4.simulation.controller.commands.SetMassCommand;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import com.example.lab4.simulation.controller.events.MassChangedEvent;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.view.AbstractWindow;
import com.example.lab4.simulation.view.SimulationUIState;
import com.example.lab4.simulation.view.UIStateObserver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControlWindow extends AbstractWindow implements SimulationObserver, UIStateObserver {

    private final SimulationController controller;
    private ObserverRegistration registration;

    private Slider massSlider;
    private Slider throttleSlider;
    private Slider gravitySlider;

    private boolean massSliderBeingDragged = false;

    public ControlWindow(SimulationController controller) {
        this.controller = controller;

    }

    @Override
    public void onUIStateChanged(SimulationUIState newState) {
        Platform.runLater(() -> {
            boolean massEditable = newState == SimulationUIState.STOPPED;
            massSlider.setDisable(!massEditable);
        });
    }


    public void show() {
        stage = new Stage();
        stage.setTitle("Controls");

        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(10));

        // -------- MASS --------
        Label massLabel = new Label("Mass (kg)");
        massSlider = new Slider(1, 1000, 1);
        massSlider.setShowTickLabels(true);
        massSlider.setShowTickMarks(true);
        massSlider.setBlockIncrement(1);
        massSlider.setMajorTickUnit(200);
        massSlider.setMinorTickCount(0);

        //------- GRAVITY ---------
        Label gravityLabel = new Label("Gravity (g)");
        gravitySlider = new Slider(1,10,1);
        gravitySlider.setShowTickLabels(true);
        gravitySlider.setShowTickMarks(true);
        gravitySlider.setBlockIncrement(1);
        gravitySlider.setMajorTickUnit(1);
        gravitySlider.setMinorTickCount(0);

        // -------- THROTTLE --------
        Label throttleLabel = new Label("Throttle Force");
        double initialMaxThrottle = 5;
        throttleSlider = new Slider(-initialMaxThrottle, initialMaxThrottle, 0);
        throttleSlider.setShowTickLabels(true);
        throttleSlider.setShowTickMarks(true);
        throttleSlider.setMajorTickUnit((initialMaxThrottle * 2) / 5);
        throttleSlider.setMinorTickCount(0);






        // --- Listeners (копия логики) ---
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (massSliderBeingDragged) return;

            double mass = Math.round(newVal.doubleValue());
            controller.execute(new SetMassCommand(mass));

            updateThrottleLimits(mass);
        });

        massSlider.setOnMousePressed(e -> massSliderBeingDragged = true);

        massSlider.setOnMouseReleased(e -> {
            massSliderBeingDragged = false;

            double mass = Math.round(massSlider.getValue());
            controller.execute(new SetMassCommand(mass));

            updateThrottleLimits(mass);
        });

        throttleSlider.valueProperty().addListener((obs, o, n) -> {
            if (!massSliderBeingDragged) {
                controller.execute(new SetThrottleCommand(n.doubleValue()));
            }
        });

        gravitySlider.valueProperty().addListener((obs, o, n) -> {
            controller.execute(new SetGravityCommand(n.doubleValue()));
        });

        root.getChildren().addAll(
                massLabel, massSlider,
                throttleLabel, throttleSlider,
                gravityLabel, gravitySlider
        );

        stage.setOnCloseRequest(e -> {
            handleClose();
        });

        stage.setScene(new Scene(root, 300, 250));
        stage.show();

    }

    /** Закрытие окна */
    public void handleClose() {
        System.out.println("[ControlWindow] closing the window");
        if (registration != null) registration.unregister();
        if (onCloseCallback != null) onCloseCallback.run();
        if (onClosedByItself != null) onClosedByItself.run();
    }

    public void setRegistration(ObserverRegistration registration) {
        this.registration = registration;
    }

    private void updateThrottleLimits(double mass) {
        double maxThrottle = mass * 5;
        throttleSlider.setMin(-maxThrottle);
        throttleSlider.setMax(maxThrottle);
        throttleSlider.setMajorTickUnit((maxThrottle * 2) / 5);

        if (throttleSlider.getValue() > throttleSlider.getMax())
            throttleSlider.setValue(throttleSlider.getMax());
        if (throttleSlider.getValue() < throttleSlider.getMin())
            throttleSlider.setValue(throttleSlider.getMin());
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof MassChangedEvent e) {
            Platform.runLater(() -> {
                double mass = e.getNewMass();
                massSlider.setValue(mass);
                updateThrottleLimits(mass);
            });
        }
    }
}
