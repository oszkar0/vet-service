package oskar.vetservice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
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

        //find owner in owners list
        int ownerId = selectedAnimal.getOwnerId();
        Owner animalsOwner = owners.stream()
                .filter(owner -> {return owner.getId() == ownerId;})
                .findFirst()
                .orElse(null);

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
                    animalsOwner.getName() , animalsOwner.getSurname() , animalsOwner.getEmail() , animalsOwner.getPhoneNumber(), animalsOwner.getCity(), animalsOwner.getStreet()
                    , animalsOwner.getHouseNumber());
            stage.showAndWait();

        } catch (IOException e) {
            System.out.println("Error loading AnimalsDetailsView.fxml: " + e.getMessage());
        }

    }

    public void deleteSelectedAnimal(){
        Animal selectedAnimal = animalsTableView.getSelectionModel().getSelectedItem();

        if(selectedAnimal == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No animal selected");
            alert.setHeaderText("You didn't select the animal");
            alert.showAndWait();
            return;
        }

        try {
            if(DataSource.getInstance().deleteAnimalByItsId(selectedAnimal.getId())){
                Path deletePhotoPath = Paths.get(selectedAnimal.getPhotoPath());
                try {
                    Files.delete(deletePhotoPath);
                } catch (IOException e){
                    System.out.println("Error deleting animals photo: " + e.getMessage());
                }

                animals.remove(selectedAnimal);
            }
        } catch(SQLException e) {
            System.out.println("Error deleting animal: " + e.getMessage());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Animal deleted successfully");
        alert.setHeaderText("Animal deleted successfully");
        alert.showAndWait();
    }

    public void deleteSelectedOwnerAndHisAnimals(){
        Owner selectedOwner = ownersTableView.getSelectionModel().getSelectedItem();

        if(selectedOwner == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No owner selected");
            alert.setHeaderText("You didn't select the animal's owner");
            alert.showAndWait();
            return;
        }

        //find owners animals in animals table view
        Animal[] ownersAnimalsInTableView = animals.stream()
                                                  .filter(animal -> {return animal.getOwnerId() == selectedOwner.getId();})
                                                  .toArray(Animal[]::new);
        //turn off autocommit
        DataSource.getInstance().turnAutoCommit(false);

        try{
            List<Animal> animalsToDelete = DataSource.getInstance().gueryAnimalsByItsOwnerId(selectedOwner.getId());
            DataSource.getInstance().deleteAnimalsByItsOwnerId(selectedOwner.getId());
            //delete animals from table view
            for(Animal animal: ownersAnimalsInTableView){
                animals.remove(animal);
            }

            //delete animals photos using path from db as animals ObservableArray
            //can contain only others owner animals
            for(Animal animal: animalsToDelete){
                Path deletePhotoPath = Paths.get(animal.getPhotoPath());
                Files.delete(deletePhotoPath);
            }
        } catch (SQLException| IOException  e){
            System.out.println("Error deleting owners animals: " + e.getMessage());
            DataSource.getInstance().rollback();
            DataSource.getInstance().turnAutoCommit(true);
            return;
        }

        try{
            DataSource.getInstance().deleteOwnerByHisId(selectedOwner.getId());
            owners.remove(selectedOwner);
        } catch (SQLException e){
            System.out.println("Error deleting owner: " + e.getMessage());
            DataSource.getInstance().rollback();
            DataSource.getInstance().turnAutoCommit(true);
            return;
        }

        //commit changes and turn autocommit on
        DataSource.getInstance().commit();
        DataSource.getInstance().turnAutoCommit(true);

    }
}