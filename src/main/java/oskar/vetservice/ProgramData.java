package oskar.vetservice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import oskar.vetservice.model.Animal;
import oskar.vetservice.model.Owner;

public class ProgramData {
    ObservableList<Animal> animals;
    ObservableList<Owner> owners;
    private static ProgramData instance = new ProgramData();

    private ProgramData(){
        animals = FXCollections.observableArrayList();
        owners = FXCollections.observableArrayList();
    }

    public static ProgramData getInstance() {
        return instance;
    }
}
