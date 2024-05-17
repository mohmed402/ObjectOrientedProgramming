module com.example.surgery {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.surgery to javafx.fxml;
    exports com.example.surgery;
}