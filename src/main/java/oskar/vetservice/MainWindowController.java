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


}