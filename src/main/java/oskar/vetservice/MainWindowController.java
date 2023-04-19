package oskar.vetservice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
            owners.add(owner);
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
            //result processing
        }
    }
}