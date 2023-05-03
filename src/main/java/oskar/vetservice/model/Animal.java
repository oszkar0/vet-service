package oskar.vetservice.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Date;

public class Animal {

    private int id;
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty species = new SimpleStringProperty("");
    private SimpleStringProperty gender = new SimpleStringProperty("");
    String photoPath;
    private LocalDate birthday;
    private int ownerId;


    public Animal(int id,String name, String species, String gender, LocalDate birthday, int ownerId, String photoPath) {
        this.id = id;
        this.name.set(name);
        this.species.set(species);
        this.gender.set(gender);
        this.birthday = birthday;
        this.ownerId = ownerId;
        this.photoPath = photoPath;
    }

    public Animal(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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

    public String getSpecies() {
        return species.get();
    }

    public SimpleStringProperty speciesProperty() {
        return species;
    }

    public void setSpecies(String species) {
        this.species.set(species);
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
