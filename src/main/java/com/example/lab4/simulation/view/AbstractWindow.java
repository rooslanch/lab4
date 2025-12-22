package com.example.lab4.simulation.view;

import javafx.stage.Stage;

public abstract class AbstractWindow {
    protected Stage stage;
    protected Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }


    public void handleClose() {

        if (onCloseCallback != null) onCloseCallback.run();
    }
    /** Унифицированное закрытие окна */
    public void close() {
        System.out.println("[AbstractWindow] closing window");
        if (stage != null) {
            System.out.println("[AbstractWindow] stage!=0 while closing window");
            handleClose();  // вызываем логику отписки и коллбеков
            stage.close();   // закрываем Stage
        }
    }

    public abstract void show();
}
