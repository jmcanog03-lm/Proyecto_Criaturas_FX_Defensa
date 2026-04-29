
 /**
     * @author Emilio
     * @author Fabricio
     * @author JoseManuel
     * @version 1.0
     * @since 1.0
     */

module com.example {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.base;
    requires java.desktop;
    opens com.example to javafx.fxml;
    opens com.example.Modelo to com.google.gson;
    exports com.example;
    exports com.example.Modelo;


    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;



}
