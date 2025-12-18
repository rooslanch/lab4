package com.example.lab4.simulation.view.main;

import com.example.lab4.simulation.controller.SimulationController;
import com.example.lab4.simulation.controller.commands.ResetSimulationCommand;
import com.example.lab4.simulation.controller.commands.SetMassCommand;
import com.example.lab4.simulation.controller.commands.SetPausedCommand;
import com.example.lab4.simulation.controller.commands.SetThrottleCommand;
import com.example.lab4.simulation.controller.events.MassChangedEvent;
import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainViewController implements SimulationObserver {

    private final SimulationController controller;

    private Slider massSlider;
    private Slider throttleSlider;

    // Флаг для отслеживания состояния слайдера массы
    private boolean massSliderBeingDragged = false;

    public MainViewController(SimulationController controller) {
        this.controller = controller;
        // подписка на события
        controller.addObserver(this);
    }

    public VBox createControlPanel() {
        VBox leftPanel = new VBox(20);

        // --------- Кнопки ---------
        HBox buttons = new HBox(10);
        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button pauseBtn = new Button("Pause");
        Button resumeBtn = new Button("Resume");
        Button resetBtn = new Button("Reset");

        startBtn.setOnAction(e -> {
            massSlider.setDisable(true); // блокируем изменение массы
            controller.start();
        });

        stopBtn.setOnAction(e -> {
            controller.stop();
            massSlider.setDisable(false); // разблокируем
        });

        pauseBtn.setOnAction(e -> controller.execute(new SetPausedCommand(true)));
        resumeBtn.setOnAction(e -> controller.execute(new SetPausedCommand(false)));

        resetBtn.setOnAction(e -> {
            controller.execute(new ResetSimulationCommand());
            massSlider.setDisable(false);
        });

        buttons.getChildren().addAll(startBtn, stopBtn, pauseBtn, resumeBtn, resetBtn);


        Label massLabel = new Label("Mass (kg)");
        massSlider = new Slider(1, 1000, 1); // по умолчанию 1
        massSlider.setShowTickLabels(true);
        massSlider.setShowTickMarks(true);
        massSlider.setBlockIncrement(1);
        massSlider.setMajorTickUnit(200);
        massSlider.setMinorTickCount(0);

        // Слушаем событие при изменении значения массы
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Если слайдер массы сейчас перетаскивается (пользователь его двигает), то не обновляем тягу
            if (massSliderBeingDragged) {
                return;
            }

            double mass = Math.round(newVal.doubleValue());
            controller.execute(new SetMassCommand(mass));

            // пересчитываем границы для тяги
            double maxThrottle = mass * 5;
            throttleSlider.setMin(-maxThrottle);
            throttleSlider.setMax(maxThrottle);
            throttleSlider.setMajorTickUnit((maxThrottle * 2) / 5);
            throttleSlider.setMinorTickCount(0);

            // фиксируем текущее значение, если оно вышло за новые границы
            if (throttleSlider.getValue() > throttleSlider.getMax())
                throttleSlider.setValue(throttleSlider.getMax());
            if (throttleSlider.getValue() < throttleSlider.getMin())
                throttleSlider.setValue(throttleSlider.getMin());
        });

        // Добавим обработку событий для "нажатия" и "отпускания" слайдера массы
        massSlider.setOnMousePressed(event -> {
            // Когда пользователь нажимает на слайдер массы, блокируем обновление тяги
            massSliderBeingDragged = true;
        });

        massSlider.setOnMouseReleased(event -> {
            // Когда пользователь отпускает слайдер массы, восстанавливаем обновление тяги
            massSliderBeingDragged = false;

            // Выполняем обновление слайдера тяги с новыми значениями
            double mass = Math.round(massSlider.getValue());
            controller.execute(new SetMassCommand(mass));

            // Пересчитываем границы для тяги
            double maxThrottle = mass * 5;
            throttleSlider.setMin(-maxThrottle);
            throttleSlider.setMax(maxThrottle);
            throttleSlider.setMajorTickUnit((maxThrottle * 2) / 5);
            throttleSlider.setMinorTickCount(0);

            // Исправляем текущие значения, если они выходят за пределы
            if (throttleSlider.getValue() > throttleSlider.getMax())
                throttleSlider.setValue(throttleSlider.getMax());
            if (throttleSlider.getValue() < throttleSlider.getMin())
                throttleSlider.setValue(throttleSlider.getMin());
        });

        // --------- THROTTLE SLIDER ---------
        Label throttleLabel = new Label("Throttle Force");
        double initialMaxThrottle = 5; // при массе 1
        throttleSlider = new Slider(-initialMaxThrottle, initialMaxThrottle, 0);
        throttleSlider.setShowTickLabels(true);
        throttleSlider.setShowTickMarks(true);
        throttleSlider.setMajorTickUnit((initialMaxThrottle * 2) / 5);
        throttleSlider.setMinorTickCount(0);

        throttleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Изменения тяги не должны происходить, пока слайдер массы перетаскивается
            if (!massSliderBeingDragged) {
                controller.execute(new SetThrottleCommand(newVal.doubleValue()));
            }
        });

        leftPanel.getChildren().addAll(
                buttons,
                massLabel, massSlider,
                throttleLabel, throttleSlider
        );

        return leftPanel;
    }

    /** Обновление слайдера массы через MassChangedEvent */
    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof MassChangedEvent massEvent) {
            Platform.runLater(() -> {
                double mass = massEvent.getNewMass();
                massSlider.setValue(mass);

                // пересчитываем границы для тяги
                double maxThrottle = mass * 5;
                throttleSlider.setMin(-maxThrottle);
                throttleSlider.setMax(maxThrottle);
            });
        }
    }
}

