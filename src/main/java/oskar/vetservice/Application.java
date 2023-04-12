package oskar.vetservice;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import oskar.vetservice.model.DataSource;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/MainWindowView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.getIcons().add(new Image(Application.class.getResourceAsStream("icon/caticon.png")));
        stage.setTitle("VetService");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!DataSource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }


    public static void main(String[] args) {
        launch();
    }
}