package oskar.vetservice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import oskar.vetservice.model.Animal;
import oskar.vetservice.model.DataSource;
import oskar.vetservice.startup.StartupData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddNewAnimalController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField speciesTextField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private DialogPane newAnimalWindow;
    @FXML
    private Label photoLabel;

    private File choosenPhoto;

    @FXML
    public void chooseAnimalsPhoto(){
        FileChooser photoChooser =  new FileChooser();
        photoChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Photo", "*.jpg", "*.png")
        );

        choosenPhoto = photoChooser.showOpenDialog(newAnimalWindow.getScene().getWindow());

        if(choosenPhoto != null){
            photoLabel.setTextFill(Color.GREEN);
            photoLabel.setText("You have choosen a photo: " + choosenPhoto.getAbsolutePath());
        } else {
            photoLabel.setTextFill(Color.RED);
            photoLabel.setText("You have not choosen animals photo");
        }
    }

    public Animal processAnimalAdding(int ownerId){
        String name = nameTextField.getText();
        String species = speciesTextField.getText();
        String gender = genderComboBox.getValue();
        LocalDate birthday = birthdayDatePicker.getValue();
        String pathToPhoto;

        if(choosenPhoto != null){
            Path sourcePath = choosenPhoto.toPath();
            Path destinationPath = Paths.get("animalsphotos\\"
                    + StartupData.getInstance().getNextPhotoId() + choosenPhoto.getName().substring(choosenPhoto.getName().lastIndexOf('.')));
            try {
                Files.copy(sourcePath, destinationPath);
                //we save the photo in animalsphotos folder and save the relative path to it
                pathToPhoto = Paths.get("").toAbsolutePath().relativize(destinationPath.toAbsolutePath()).toString();
            }catch (IOException e){
                System.out.println("Error copying an image to animalsphotos folder: " + e.getMessage());
                pathToPhoto = "none";
            }
        } else {
            pathToPhoto = "none";
        }

        try {
            if (!DataSource.getInstance().ownerHasAnimal(name, birthday, ownerId)) {
                int createdId = DataSource.getInstance().insertAnimal(name, ownerId,birthday,species,gender,pathToPhoto);
                return new Animal(name, species, gender, birthday, ownerId, pathToPhoto);
            } else {
                //add alert if owner has an animal
                return null;
            }
        } catch (SQLException e){
            System.out.println("Error querying database in animal adding processing: " + e.getMessage());
            return null;
        }
    }




}
