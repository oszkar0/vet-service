package oskar.vetservice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oskar.vetservice.model.Animal;
import oskar.vetservice.model.DataSource;
import oskar.vetservice.model.Owner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainWindowController {
    @FXML
    private TableView<Animal> animalsTableView;
    @FXML
    private TableView<Owner> ownersTableView;
    @FXML
    private BorderPane mainWindow;

    ObservableList<Owner> owners;
    ObservableList<Animal> animals;
    @FXML
    public void initialize(){
        owners = FXCollections.observableArrayList();
        ownersTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ownersTableView.setItems(owners);
        try {
            owners.setAll(DataSource.getInstance().getAllOwners());
        } catch(SQLException e){
            System.out.println("Error getting owners from db: " + e.getMessage());
            Platform.exit();
        }

        animals = FXCollections.observableArrayList();
        animalsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        animalsTableView.setItems(animals);
        try {
            animals.setAll(DataSource.getInstance().getAllAnimals());
        } catch(SQLException e){
            System.out.println("Error getting animals from db: " + e.getMessage());
            Platform.exit();
        }
    }
    @FXML
    public void showAddNewOwnerWindow(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add new owner");
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("views/AddNewOwnerView.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load the new owner dialog: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            AddNewOwnerController controller = fxmlLoader.getController();
            Owner owner = controller.processOwnerAdding();
            if(owner != null) {
                owners.add(owner);
            }
        }
    }

    @FXML
    public void showAddNewAnimalWindow(){
        Owner selectedOwner = ownersTableView.getSelectionModel().getSelectedItem();

        if(selectedOwner == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No owner selected");
            alert.setHeaderText("You didn't select the animal's owner");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add new animal to " + selectedOwner.getName() + " " + selectedOwner.getSurname());
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("views/AddNewAnimalView.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load the new animal dialog: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            AddNewAnimalController controller = fxmlLoader.getController();
            Animal animal = controller.processAnimalAdding(selectedOwner.getId());
            if(animal != null){
                animals.add(animal);
            }
        }
    }

    @FXML
    public void showAnimalsDetailsWindow(){
        Animal selectedAnimal = animalsTableView.getSelectionModel().getSelectedItem();

        if(selectedAnimal == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No animal selected");
            alert.setHeaderText("You didn't select the animal");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/AnimalsDetailsView.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(selectedAnimal.getName() + " details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainWindow.getScene().getWindow());
            stage.setScene(new Scene(root));

            AnimalsDetailsController controller = fxmlLoader.getController();
            controller.setContent(selectedAnimal.getPhotoPath(), selectedAnimal.getName() ,selectedAnimal.getSpecies(), selectedAnimal.getBirthday(), selectedAnimal.getGender(),
                    "x" , "x" , "x" , "x", "x ", "y", "y");
            stage.showAndWait();

        } catch (IOException e) {
            System.out.println("Error loading AnimalsDetailsView.fxml: " + e.getMessage());
        }

    }
}