package main.others.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * Created by User on 28.05.2018.
 */
public class AlertWindows
{
    private static Alert customButtonsAlert;
    private static Alert errorAlert;
    private static Alert infoAlert;
    private static Alert acceptAlert;

    private static ButtonType buttonYes=new ButtonType("Да");
    private static ButtonType buttonNo=new ButtonType("Нет");
    static
    {
        customButtonsAlert=new Alert(Alert.AlertType.CONFIRMATION);
        customButtonsAlert.setResizable(false);
        customButtonsAlert.setTitle(null);
        customButtonsAlert.setHeaderText(null);
        customButtonsAlert.setContentText(null);
        customButtonsAlert.initModality(Modality.APPLICATION_MODAL);
        customButtonsAlert.getButtonTypes().clear();

        errorAlert=new Alert(Alert.AlertType.ERROR);
        errorAlert.setResizable(false);
        errorAlert.setTitle(null);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(null);
        errorAlert.initModality(Modality.APPLICATION_MODAL);

        infoAlert=new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setResizable(false);
        infoAlert.setTitle(null);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(null);
        infoAlert.initModality(Modality.APPLICATION_MODAL);


        acceptAlert=new Alert(Alert.AlertType.CONFIRMATION);
        acceptAlert.setResizable(false);
        acceptAlert.setTitle(null);
        acceptAlert.setHeaderText(null);
        acceptAlert.setContentText(null);
        acceptAlert.initModality(Modality.APPLICATION_MODAL);
        acceptAlert.getButtonTypes().setAll(buttonYes, buttonNo);

    }

    public static Optional<ButtonType> showAndWaitCustomButtonsAlert(String title, String header, String content, ButtonType ... buttons)
    {
        customButtonsAlert.setTitle(title);
        customButtonsAlert.setHeaderText(header);
        customButtonsAlert.setContentText(content);
        customButtonsAlert.getButtonTypes().setAll(buttons);
        return customButtonsAlert.showAndWait();
    }

    public static void showErrorAlert(String title, String header, String content)
    {
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    public static void showInfoAlert(String title, String header, String content)
    {
        infoAlert.setTitle(title);
        infoAlert.setHeaderText(header);
        infoAlert.setContentText(content);
        infoAlert.showAndWait();
    }

    public static boolean showAcceptAlert(String title, String header, String content)
    {
        acceptAlert.setTitle(title);
        acceptAlert.setHeaderText(header);
        acceptAlert.setContentText(content);

        Optional<ButtonType> result = acceptAlert.showAndWait();
        if(result.isPresent())
        {
            if(result.get()==buttonYes)
                return true;
        }
        return false;
    }
}
