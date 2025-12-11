module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.lab4.simulation to javafx.fxml;  // Открываем только simulation для JavaFX
    exports com.example.lab4.simulation;  // Экспортируем только simulation
}
