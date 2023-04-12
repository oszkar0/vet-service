package oskar.vetservice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import oskar.vetservice.model.DataSource;

import java.io.File;
import java.time.LocalDate;

public class AddNewAnimalController {
    @FXML
    private DialogPane newAnimalWindow;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField speciesTextField;
    @FXML
    private ComboBox<String> gednerComboBox;
    @FXML
    private DatePicker birthdayDatePicker;

    private File photoChooserChoice;
    @FXML
    public void showPhotoChooser(){
        FileChooser photoChooser = new FileChooser();
        photoChooser.setTitle("Choose photo for the animal");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg" )
        );
        photoChooserChoice = photoChooser.showOpenDialog(newAnimalWindow.getScene().getWindow());

    }

    public void processResult(int ownerId){
        //get the values from window
        String name = nameTextField.getText();
        String species = speciesTextField.getText();
        LocalDate birthday = birthdayDatePicker.getValue();
        String gender = gednerComboBox.getValue();

        //we dont have to check if owner exists because he's chosen from the tableview
        //we check if the animal is already added to the base (owner is not allowed to have
        //an animal with the same name and birthday

        if( DataSource.getInstance().ownerHasAnimal(name.toLowerCase(), birthday, ownerId )){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("That animal is already in the database");
        } else {

        }
    }

}
