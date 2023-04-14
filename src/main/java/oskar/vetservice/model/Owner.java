package oskar.vetservice.model;

import javafx.beans.property.SimpleStringProperty;

public class Owner {
    SimpleStringProperty name = new SimpleStringProperty("");
    SimpleStringProperty surname = new SimpleStringProperty("");
    SimpleStringProperty phoneNumber = new SimpleStringProperty("");
    SimpleStringProperty email = new SimpleStringProperty("");

    String city;
    String street;
    String houseNumber;
}
