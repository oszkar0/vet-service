package oskar.vetservice;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import oskar.vetservice.model.DataSource;
import oskar.vetservice.model.Owner;

import java.sql.SQLException;

public class AddNewOwnerController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField  surnameTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;

    public Owner processOwnerAdding(){
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String city = cityTextField.getText();
        String street = streetTextField.getText();
        String houseNumber = houseNumberTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();

        try {
            if (!DataSource.getInstance().ownerExists(name, surname, phoneNumber)) {
                int createdId = DataSource.getInstance().insertOwner(name, surname, city,
                        street, houseNumber, phoneNumber, email);
                return new Owner(createdId, name, surname, phoneNumber, email, city, street, houseNumber);
            } else{
                return null;
            }
        } catch (SQLException e){
            System.out.println("Error adding owner:" +  e.getMessage());
            return null;
        }
    }
}
