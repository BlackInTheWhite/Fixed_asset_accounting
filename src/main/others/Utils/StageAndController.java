package main.others.Utils;

import javafx.stage.Stage;

/**
 * Created by User on 28.05.2018.
 */
public class StageAndController
{
    private Stage stage;
    private Object controller;
    StageAndController(Stage stage, Object controller)
    {
        this.stage=stage;
        this.controller=controller;
    }

    public Stage getStage() {
        return stage;
    }

    public Object getController() {
        return controller;
    }
}
