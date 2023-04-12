package oskar.vetservice.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Date;

public class Animal {

    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty species = new SimpleStringProperty("");
    private SimpleStringProperty gender = new SimpleStringProperty("");;
    private LocalDate dateOfBirth;
    private String ownerId;
    private int age;

    public Animal(SimpleStringProperty name, SimpleStringProperty breed, SimpleStringProperty gender, LocalDate dateOfBirth, String ownerId) {
        this.name = name;
        this.species = breed;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.ownerId = ownerId;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBreed() {
        return species.get();
    }

    public SimpleStringProperty breedProperty() {
        return species;
    }

    public void setBreed(String breed) {
        this.species.set(breed);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
