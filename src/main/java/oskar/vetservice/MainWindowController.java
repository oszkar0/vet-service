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
    @FXML
    private Label viewingModelLabel;
    @FXML
    private Button viewAllAnimalsButton;

    ObservableList<Owner> owners;
    ObservableList<Animal> animals;
    @FXML
    public void initialize(){
        owners = FXCollections.observableArrayList();
        ownersTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ownersTableView.setItems(owners);
        getAndSetAllOwners();

        animals = FXCollections.observableArrayList();
        animalsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        animalsTableView.setItems(animals);
        getAndSetAllAnimals();
        viewAllAnimalsButton.setDisable(true);
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

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete " + selectedAnimal.getName() + "?");
        confirmation.setHeaderText("Are you sure you want to delete " + selectedAnimal.getName() + "?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (DataSource.getInstance().deleteAnimalByItsId(selectedAnimal.getId())) {
                    String photoPath = selectedAnimal.getPhotoPath();
                    //handle situation when photo might not have been added
                    if (!photoPath.equalsIgnoreCase("none")) {
                        try {
                            Path deletePhotoPath = Paths.get(photoPath);
                            Files.delete(deletePhotoPath);
                        } catch (IOException e) {
                            System.out.println("Error deleting animals photo: " + e.getMessage());
                        }
                    }

                    animals.remove(selectedAnimal);
                }
            } catch (SQLException e) {
                System.out.println("Error deleting animal: " + e.getMessage());
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Animal deleted successfully");
            alert.setHeaderText("Animal deleted successfully");
            alert.showAndWait();
        }
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

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete " + selectedOwner.getName() + " " + selectedOwner.getSurname() + "?");
        confirmation.setHeaderText("Are you sure you want to delete " + selectedOwner.getName() + " " + selectedOwner.getSurname() +
                " and his/her animals?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            //find owners animals in animals table view
            Animal[] ownersAnimalsInTableView = animals.stream()
                    .filter(animal -> {
                        return animal.getOwnerId() == selectedOwner.getId();
                    })
                    .toArray(Animal[]::new);
            //turn off autocommit
            DataSource.getInstance().turnAutoCommit(false);

            try {
                List<Animal> animalsToDelete = DataSource.getInstance().queryAnimalsByItsOwnerId(selectedOwner.getId());
                DataSource.getInstance().deleteAnimalsByItsOwnerId(selectedOwner.getId());
                //delete animals from table view
                for (Animal animal : ownersAnimalsInTableView) {
                    animals.remove(animal);
                }

                //delete animals photos using path from db as animals ObservableArray
                //can contain only others owner animals
                for (Animal animal : animalsToDelete) {
                    String photoPath = animal.getPhotoPath();
                    //handle situation where photo might not have been added
                    if (!photoPath.equalsIgnoreCase("none")) {
                        Path deletePhotoPath = Paths.get(photoPath);
                        Files.delete(deletePhotoPath);
                    }
                }
            } catch (SQLException | IOException e) {
                System.out.println("Error deleting owners animals: " + e.getMessage());
                DataSource.getInstance().rollback();
                DataSource.getInstance().turnAutoCommit(true);
                return;
            }

            try {
                DataSource.getInstance().deleteOwnerByHisId(selectedOwner.getId());
                owners.remove(selectedOwner);
            } catch (SQLException e) {
                System.out.println("Error deleting owner: " + e.getMessage());
                DataSource.getInstance().rollback();
                DataSource.getInstance().turnAutoCommit(true);
                return;
            }

            //commit changes and turn autocommit on
            DataSource.getInstance().commit();
            DataSource.getInstance().turnAutoCommit(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Owner deleted successfully");
            alert.setHeaderText("Owner deleted successfully");
            alert.showAndWait();
        }
    }
    @FXML
    private void getAndSetAllAnimals(){
        Task<List<Animal>> getAllAnimalsTask = new Task<List<Animal>>() {
            @Override
            protected List<Animal> call() throws Exception {
               return DataSource.getInstance().getAllAnimals();
            }

            @Override
            protected void succeeded() {
                animals.setAll(getValue());
            }

            @Override
            protected void failed() {
                Throwable exception = getException();
                if(exception instanceof SQLException){
                    System.out.println("Error getting animals from database: " + exception.getMessage());
                } else {
                    System.out.println("Unexpected exception occurred: " + exception.getMessage());
                }
                Platform.exit();
            }
        };

        new Thread(getAllAnimalsTask).start();
    }
    @FXML
    private void getAndSetAllOwners() {
        Task<List<Owner>> getAllOwnersTask = new Task<List<Owner>>() {
            @Override
            protected List<Owner> call() throws Exception {
                return DataSource.getInstance().getAllOwners();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                owners.setAll(getValue());
            }

            @Override
            protected void failed() {
                super.failed();
                Throwable exception = getException();
                if(exception instanceof SQLException){
                    System.out.println("Error getting owners from database: " + exception.getMessage());
                } else {
                    System.out.println("Unexpected exception occurred: " + exception.getMessage());
                }
                Platform.exit();
            }
        };

        new Thread(getAllOwnersTask).start();
    }

    @FXML
    public void viewSelectedPersonsAnimals(){
        Owner selectedOwner = ownersTableView.getSelectionModel().getSelectedItem();

        if(selectedOwner == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No owner selected");
            alert.setHeaderText("You didn't select the animal's owner");
            alert.showAndWait();
            return;
        }

        try {
            List<Animal> selectedPersonsAnimals = DataSource.getInstance().queryAnimalsByItsOwnerId(selectedOwner.getId());
            animals.setAll(selectedPersonsAnimals);
        } catch (SQLException e){
            System.out.println("Error getting selected person's animals from db: " + e.getMessage());
            return;
        }

        viewingModelLabel.setText("Viewing only " + selectedOwner.getName() + " " + selectedOwner.getSurname()
                + "'s animals" );
        viewAllAnimalsButton.setDisable(false);
    }

    @FXML
    public void viewAllAnimals(){
        getAndSetAllAnimals();
        viewingModelLabel.setText("Viewing all animals");
        viewAllAnimalsButton.setDisable(true);
    }
}