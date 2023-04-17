package oskar.vetservice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import oskar.vetservice.model.Animal;
import oskar.vetservice.model.DataSource;
import oskar.vetservice.model.Owner;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController {
    @FXML
    private TableView<Animal> animalsTableView;
    @FXML
    private TableView<Owner> ownersTableView;
    @FXML
    private BorderPane mainWindow;

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
            //add results processing
        }
    }
}