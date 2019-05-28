package main.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import main.Controllers.Edit.EditMovementsController;
import main.Controllers.Edit.EditRepairCostsController;
import main.Controllers.Edit.EditTransfersController;
import main.others.Utils.AlertWindows;
import main.others.Utils.EditTypes;
import main.others.Utils.StageAndController;
import main.others.Utils.StageAndControllerInitializer;

import java.util.Optional;

/**
 * Created by User on 28.05.2018.
 */
public class CardController
{
    private Stage receptionObjectStage, editTransfersStage, editMovementsStage, writeoffObjectStage,
            depreciationObjectStage, editRepairCostsStage,cardInformationStage, directoryStage, reportsStage;

    private ReceptionObjectController receptionObjectController;
    private EditTransfersController editTransfersController;
    private EditMovementsController editMovementsController;
    private WriteOffObjectController writeOffObjectController;
    private DepreciationObjectController depreciationObjectController;
    private EditRepairCostsController editRepairCostsController;
    private CardInformationController cardInformationController;
    private ReportsController reportsController;

    private DirectoryController directoryController;
    private static Stage stage;

    private ButtonType buttonNew=new ButtonType("Новый");
    private ButtonType buttonNotNew=new ButtonType("Используемый");
    private ButtonType buttonCancel=new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
    private boolean objectIsNew;

    @FXML
    public void initialize()
    {
        StageAndController sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/receptionObject.fxml"), "Принятие объекта");
        receptionObjectStage=sac.getStage();
        receptionObjectController=(ReceptionObjectController)sac.getController();
        receptionObjectController.setStage(receptionObjectStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editTransfers.fxml"), "Передача объекта другой организации");
        editTransfersStage =sac.getStage();
        editTransfersController=(EditTransfersController)sac.getController();
        editTransfersController.setStage(editTransfersStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editMovements.fxml"), "Внутренее перемещение");
        editMovementsStage =sac.getStage();
        editMovementsController =(EditMovementsController)sac.getController();
        editMovementsController.setStage(editMovementsStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/writeoffobject.fxml"), "Списание объекта");
        writeoffObjectStage=sac.getStage();
        writeOffObjectController=(WriteOffObjectController)sac.getController();
        writeOffObjectController.setStage(writeoffObjectStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/depreciation.fxml"), "Начисление амортизации");
        depreciationObjectStage=sac.getStage();
        depreciationObjectController=(DepreciationObjectController) sac.getController();
        depreciationObjectController.setStage(depreciationObjectStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editRepairCost.fxml"), "Затраты на ремонт");
        editRepairCostsStage =sac.getStage();
        editRepairCostsController =(EditRepairCostsController) sac.getController();
        editRepairCostsController.setStage(editRepairCostsStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/directory.fxml"), "Справочники");
        directoryStage=sac.getStage();
        directoryController=(DirectoryController) sac.getController();
        directoryController.setStage(directoryStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/cardInformation.fxml"), "Информация по инвентарной карточке");
        cardInformationStage=sac.getStage();
        cardInformationController=(CardInformationController) sac.getController();
        cardInformationController.setStage(cardInformationStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/reports.fxml"), "Отчетная документация");
        reportsStage=sac.getStage();
        reportsController=(ReportsController) sac.getController();
        reportsController.setStage(reportsStage);
    }

    public static void setStage(Stage stage)
    {
        CardController.stage =stage;
    }

    @FXML
    private void btnReceptionObject(ActionEvent actionEvent)
    {
        Optional<ButtonType> result=AlertWindows.showAndWaitCustomButtonsAlert("Выберите объект",null,
                "Вы хотите принять новый объект или используемый?", buttonNew, buttonNotNew, buttonCancel);

        if(result.get()==buttonNew)
            objectIsNew=true;
        else if(result.get()==buttonNotNew)
            objectIsNew=false;
        else
            return;

        receptionObjectController.receptionObjectIsNew(objectIsNew);
        receptionObjectStage.showAndWait();
    }
    @FXML
    private void btnMovementsAction(ActionEvent actionEvent)
    {
        editMovementsController.setParameters(EditTypes.ADDING, null);
        editMovementsStage.showAndWait();
    }
    @FXML
    private void btnWriteOffObjectAction(ActionEvent actionEvent)
    {
        writeoffObjectStage.showAndWait();
    }
    @FXML
    private void btnTransferObjectAction(ActionEvent actionEvent)
    {
        editTransfersController.setParameters(EditTypes.ADDING, null);
        editTransfersStage.showAndWait();
    }
    @FXML
    private void btnRepairAction(ActionEvent actionEvent)
    {
        editRepairCostsController.setParameters(EditTypes.ADDING, null);
        editRepairCostsStage.showAndWait();
    }
    @FXML
    private void btnDepreciationAction(ActionEvent actionEvent) {
        depreciationObjectStage.showAndWait();
    }
    @FXML
    private void btnDirectoryAction(ActionEvent actionEvent) {
        directoryStage.showAndWait();
    }
    @FXML
    private void btnCardInformationAction(ActionEvent actionEvent) {
        cardInformationStage.showAndWait();
    }
    @FXML
    private void btnExitAction(ActionEvent event)
    {
        boolean result=AlertWindows.showAcceptAlert("Выход из программы",null,
                "Вы действительно хотите выйти из программы?");

        if(result)
            stage.close();
    }

    public void btnReportsAction(ActionEvent event)
    {
        reportsStage.showAndWait();
    }
}
