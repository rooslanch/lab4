package com.example.lab4.simulation.view;

import javafx.stage.Stage;

public abstract class AbstractWindow {
    protected Stage stage;
    protected Runnable onCloseCallback;
    protected Runnable onClosedByController;
    protected Runnable onClosedByItself;

    public void setOnClosedByController(Runnable r) {
        this.onClosedByController = r;
    }

    public void setOnClosedByItself(Runnable r) {
        this.onClosedByItself = r;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }


    public void handleClose() {
        if (onCloseCallback != null) onCloseCallback.run();
        if (onClosedByItself != null) {
            onClosedByItself.run();
        }

    }

    /**
     * Унифицированное закрытие окна
     */
    public void close() {
        if (onCloseCallback != null) onCloseCallback.run();
        System.out.println("[AbstractWindow] closing window");
        if (onClosedByController != null) {
            onClosedByController.run();
        }
        if (stage != null) {
            stage.close();
        }
    }

    public abstract void show();
}
