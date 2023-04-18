module oskar.vetservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;


    opens oskar.vetservice to javafx.fxml;
    opens oskar.vetservice.model to javafx.base;
    exports oskar.vetservice;
}