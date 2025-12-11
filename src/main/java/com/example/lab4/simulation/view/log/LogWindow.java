package com.example.lab4.simulation.view.log;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;

public class LogWindow {

    private static final int MAX_LINES = 500;
    private static final long UPDATE_INTERVAL_MS = 100;

    private final TextArea textArea = new TextArea();
    private final Deque<String> lines = new ArrayDeque<>();

    private final List<String> pending = Collections.synchronizedList(new ArrayList<>());

    public LogWindow() {
        Stage stage = new Stage();
        stage.setTitle("Simulation Log");

        textArea.setEditable(false);
        textArea.setWrapText(true);

        BorderPane root = new BorderPane(textArea);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        startUpdateTimer();
    }

    /** Добавляет строку в буфер */
    public void enqueue(String msg) {
        pending.add(msg);
    }

    /** Запускает обновление раз в 100 мс */
    private void startUpdateTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(UPDATE_INTERVAL_MS);
                } catch (InterruptedException ignored) {}

                flushPending();
            }
        });

        t.setDaemon(true);
        t.start();
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

                if (lines.size() > MAX_LINES) {
                    lines.removeFirst();
                }
            }

            textArea.setText(String.join("\n", lines));
            textArea.setScrollTop(Double.MAX_VALUE);
        });
    }
}
