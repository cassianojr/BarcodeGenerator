package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class for the application
 */
public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main-window.fxml"));
        primaryStage.setTitle("Gerador de Código de Barras");
        primaryStage.setScene(new Scene(root, 456, 395));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));

        primaryStage.setResizable(false);
        primaryStage.show();
        Main.stage = primaryStage;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
