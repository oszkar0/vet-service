package oskar.vetservice;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AnimalsDetailsController {
    @FXML
    private ImageView animalPhoto;
    @FXML
    private Label animalNameLabel;
    @FXML
    private Label animalSpeciesLabel;
    @FXML
    private Label animalBirthdayAndAgeLabel;
    @FXML
    private Label animalGenderLabel;
    @FXML
    private Label ownerNameAndSurnameLabel;
    @FXML
    private Label ownerEmailLabel;
    @FXML
    private Label ownerPhoneNumberLabel;
    @FXML
    private Label ownerAddressLabel;

    public void setImage(String path){
        if(path.compareTo("none") == 0){
            return;
        }

        animalPhoto.setImage(new Image("file:" + path));
        animalPhoto.setFitHeight(200);
        animalPhoto.setFitWidth(400);
    }

    public void setAnimalNameLabel(String name){
        animalNameLabel.setText(name);
    }

    public void setAnimalSpeciesLabel(String species) {
        animalSpeciesLabel.setText(species);
    }

    public void setAnimalBirthdayAndAgeLabel(LocalDate birthday) {
        int ageInMonths = (int) ChronoUnit.MONTHS.between(birthday, LocalDate.now());

        int ageInYears = ageInMonths / 12;
        int monthsLeft = ageInMonths - ageInYears * 12;

        animalBirthdayAndAgeLabel.setText(birthday.toString() + " (" + ageInYears + " years "
                + monthsLeft + " months)" );
    }

    public void setAnimalGenderLabel(String gender) {
        animalGenderLabel.setText(gender);
    }

    public void setOwnerNameAndSurnameLabel(String name, String surname) {
        ownerNameAndSurnameLabel.setText(name + " " + surname);
    }

    public void setOwnerEmailLabel(String email) {
        ownerEmailLabel.setText(email);
    }

    public void setOwnerPhoneNumberLabel(String phoneNumber) {
        ownerPhoneNumberLabel.setText(phoneNumber);
    }

    public void setOwnerAddressLabel(String city, String street, String houseNumber) {
        ownerAddressLabel.setText(city + " " + street + " " + houseNumber);
    }


    public void setContent(String imagePath, String animalName, String animalSpecies, LocalDate animalBirthday, String animalGender,
                           String ownerName, String ownerSurname, String ownerEmail, String ownerPhoneNumber, String ownerCity,
                           String ownerStreet, String ownerHouseNumber) {

        setImage(imagePath);
        setAnimalNameLabel(animalName);
        setAnimalSpeciesLabel(animalSpecies);
        setAnimalBirthdayAndAgeLabel(animalBirthday);
        setAnimalGenderLabel(animalGender);
        setOwnerNameAndSurnameLabel(ownerName, ownerSurname);
        setOwnerEmailLabel(ownerEmail);
        setOwnerPhoneNumberLabel(ownerPhoneNumber);
        setOwnerAddressLabel(ownerCity, ownerStreet, ownerHouseNumber);
    }
}
