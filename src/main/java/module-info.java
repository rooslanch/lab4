module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.lab4.simulation to javafx.fxml;  // Открываем только simulation для JavaFX
    exports com.example.lab4.simulation;  // Экспортируем только simulation
    exports com.example.lab4.simulation.persistence.dto to com.fasterxml.jackson.databind;
}
