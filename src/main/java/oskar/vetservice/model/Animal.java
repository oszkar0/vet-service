package oskar.vetservice.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Date;

public class Animal {

    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty species = new SimpleStringProperty("");
    private SimpleStringProperty gender = new SimpleStringProperty("");;
    private LocalDate birthday;
    private int ownerId;

    public Animal(String name, String species, String gender, LocalDate birthday, int ownerId) {
        this.name.set(name);
        this.species.set(species);
        this.gender.set(gender);
        this.birthday = birthday;
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
