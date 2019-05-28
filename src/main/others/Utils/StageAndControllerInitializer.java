package main.others.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

/**
 * Created by User on 28.05.2018.
 */
public class StageAndControllerInitializer
{
    public static StageAndController defaultStageInit(URL url, String title)
    {
        Stage stage=new Stage();
        FXMLLoader loader=new FXMLLoader(url);
        Parent parent=null;
        try {
            parent = (Parent) loader.load();
        }
        catch (IOException e)
        {
            System.out.println("Error with loading: "+url);
        }
        Object controller=loader.getController();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        return new StageAndController(stage, controller);
    }
}
