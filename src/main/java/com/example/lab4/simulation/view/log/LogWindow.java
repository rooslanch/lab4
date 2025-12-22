package com.example.lab4.simulation.view.log;

import com.example.lab4.simulation.controller.events.*;
import com.example.lab4.simulation.controller.observers.ObserverRegistration;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.view.AbstractWindow;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

public class LogWindow extends AbstractWindow implements SimulationObserver {

    private static final int MAX_LINES = 500;
    private static final long UPDATE_INTERVAL_MS = 100;
    private final TextArea textArea = new TextArea();
    private final Deque<String> lines = new ArrayDeque<>();
    private final List<String> pending = Collections.synchronizedList(new ArrayList<>());

    private ObserverRegistration registration;

    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    public LogWindow() {
        initStage();
        startUpdateTimer();
    }

    private void initStage() {
        stage = new Stage();
        stage.setTitle("Simulation Log");

        textArea.setEditable(false);
        textArea.setWrapText(true);

        BorderPane root = new BorderPane(textArea);
        stage.setScene(new Scene(root, 600, 400));

        stage.setOnCloseRequest(e -> handleClose());
        stage.show();
    }

    /** Добавляет строку в буфер */
    private void log(String msg) {
        pending.add("[" + sdf.format(new Date()) + "] " + msg);
    }

    /** Переливает pending → lines → TextArea (FX-поток) */
    private void flushPending() {
        if (pending.isEmpty()) return;

        List<String> batch;
        synchronized (pending) {
            batch = new ArrayList<>(pending);
            pending.clear();
        }

        Platform.runLater(() -> {
            for (String s : batch) {
                lines.addLast(s);
                if (lines.size() > MAX_LINES) lines.removeFirst();
            }
            textArea.setText(String.join("\n", lines));
            textArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void startUpdateTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try { Thread.sleep(UPDATE_INTERVAL_MS); } catch (InterruptedException ignored) {}
                flushPending();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /** SimulationObserver */
    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof StateUpdateEvent s) {
            log(String.format("State: x=%.2f, v=%.2f, a=%.2f, slope=%.2f",
                    s.getX(), s.getV(), s.getA(), s.getSlope()));
        } else if (ev instanceof BoundaryReachedEvent b) {
            log("Boundary reached: " + b.getType() + " at x=" + b.getX());
        } else if (ev instanceof RollingBackEvent r) {
            log(String.format("Rolling back: x=%.2f, v=%.2f, slope=%.2f",
                    r.getX(), r.getV(), r.getSlope()));
        } else if (ev instanceof SimulationResetEvent) {
            log("Simulation reset");
        } else if (ev instanceof ThrottleChangedEvent t) {
            log(String.format("Throttle changed: %.2f", t.getNewThrottleForce()));
        } else if (ev instanceof PositionChangedEvent p) {
            log(String.format("Position changed: x=%.2f", p.getX()));
        } else if (ev instanceof MassChangedEvent m) {
            log(String.format("Mass changed: %.2f", m.getNewMass()));
        } else if (ev instanceof FrictionChangedEvent f) {
            log(String.format("Friction changed: %.2f", f.getNewFriction()));
        }
    }

    /** Регистрация на контроллер */
    public void setRegistration(ObserverRegistration registration) {
        this.registration = registration;
    }



    /** Закрытие окна */
    public void handleClose() {
        if (registration != null) registration.unregister();
        if (onCloseCallback != null) onCloseCallback.run();
    }

    /** Показать окно */
    public void show() {
        if (stage != null && !stage.isShowing()) stage.show();
    }
}
