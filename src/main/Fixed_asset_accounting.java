package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Controllers.CardController;

/**
 * Created by User on 28.05.2018.
 */
public class Fixed_asset_accounting extends Application {
    private final static MyDatabase database=new MyDatabase();
    public static void main(String args[])
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Учет основных фондов предприятия");
        primaryStage.setResizable(false);
        Parent root= FXMLLoader.load(getClass().getResource("/main/fxml/card.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        CardController.setStage(primaryStage);
        primaryStage.setOnCloseRequest(event ->
        {
            Platform.exit();
        });
        primaryStage.show();

    }

    public static MyDatabase getMyDatabase()
    {
        return database;
    }
}
