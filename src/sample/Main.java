package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static String appName = "MIPS Simulator";

    @Override
    public void start(Stage primaryStage) throws Exception{

        // construct GUI
        Parent root = FXMLLoader.load(getClass().getResource("/res/new_gui.fxml"));
        primaryStage.setTitle(appName);
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
