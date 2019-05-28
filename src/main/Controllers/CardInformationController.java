package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Controllers.Edit.*;
import main.Fixed_asset_accounting;
import main.others.Tables.*;
import main.others.Utils.*;

import java.sql.*;

/**
 * Created by User on 08.06.2018.
 */
public class CardInformationController
{
    private Stage stage;

    @FXML
    private TabPane tpTabPane;

    @FXML
    private TableView<Transmission> twTransmisiion;

    @FXML
    private TableView<Devices> twDevices;

    @FXML
    private TableView<Movements> twMovements;

    @FXML
    private TableView<Depreciation> twDepreciation;

    @FXML
    private TableView<Transfers> twTransfer;

    @FXML
    private TableView<NobleMetals> twNobleMetals;

    @FXML
    private TableView<RepairCost> twRepairCost;

    @FXML
    private ComboBox<MyKeyValue> cbCard, cbTable;

    @FXML
    private TableView<Revaluation> twRevaluation;

    @FXML
    private TableView<CostChanges> twCostChanges;

    @FXML
    private TableView<InventoryCard> twMainTable;


    private int currendCardID=-1, currentTabID;

    private ObservableList<InventoryCard> mainTableList=FXCollections.observableArrayList();
    private ObservableList<CostChanges> costChangesList=FXCollections.observableArrayList();
    private ObservableList<Depreciation> depreciationList=FXCollections.observableArrayList();
    private ObservableList<Devices> devicesList=FXCollections.observableArrayList();
    private ObservableList<Movements> movementsList=FXCollections.observableArrayList();
    private ObservableList<NobleMetals> metalsList=FXCollections.observableArrayList();
    private ObservableList<RepairCost> repairCostList=FXCollections.observableArrayList();
    private ObservableList<Revaluation> revaluationList=FXCollections.observableArrayList();
    private ObservableList<Transmission> transmissionList=FXCollections.observableArrayList();
    private ObservableList<Transfers> transferList=FXCollections.observableArrayList();

    private Stage editCostChangeStage, editDepreciationStage, editDevicesStage, editMetalsStage,
            editRepaitCostStage, editRevaluationStage, editTransfersStage, editTransmissionStage,
            editMovementsStage, editInventoryCardStage;
    private EditCostChangesController editCostChangesController;
    private EditDepreciationController editDepreciationController;
    private EditDevicesController editDevicesController;
    private EditMetalsController editMetalsController;
    private EditRepairCostsController editRepairCostsController;
    private EditRevaluationController editRevaluationController;
    private EditTransfersController editTransfersController;
    private EditTransmissionController editTransmissionController;
    private EditMovementsController editMovementsController;
    private EditInventoryCardController editInventoryCardController;

    @FXML
    public void initialize()
    {
        int i=0;
        ObservableList<MyKeyValue> list= FXCollections.observableArrayList(new MyKeyValue(i++,"Основная таблица"),
                new MyKeyValue(i++,"Изменения стоимости"),new MyKeyValue(i++,"Амортизационные отчисления"),
                new MyKeyValue(i++,"Устройства и принадлежности"),new MyKeyValue(i++,"Внутренние перемещения"),
                new MyKeyValue(i++,"Драгоценные металлы"),new MyKeyValue(i++,"Ремонты"),
                new MyKeyValue(i++,"Переоценка"),new MyKeyValue(i++,"Передача другой организации"),
                new MyKeyValue(i++,"Прием от другой организации"));
        cbTable.setItems(list);

        String names[]={"cardNumber","organization","department","cardDate","objectName","OKUD","OKPO","OKOF",
                "derpreciationGroupNumber","passportNumber","serialNumber","inventoryNumber","deliveryDate",
                "writeoffDate","accountnumber","location","departmentNumber","manufacturer","initialCost","usefulLife",
                "crp","mrp","participants","ownershipShare","structElemnts","mainObjectCharacteristics",
                "deviceCharacteristics","notice"};
        i=0;
        for(TableColumn<InventoryCard, ?> tc:twMainTable.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twMainTable.setItems(mainTableList);

        names=new String[]{"card", "type", "name", "date", "number", "cost"}; i=0;
        for(TableColumn<CostChanges, ?> tc:twCostChanges.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twCostChanges.setItems(costChangesList);

        names=new String[]{"card","year", "m1","m2","m3","m4","m5","m6","m7","m8","m9","m10","m11","m12"}; i=0;
        for(TableColumn<Depreciation, ?> tc:twDepreciation.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twDepreciation.setItems(depreciationList);

        names=new String[]{"card", "name", "count"}; i=0;
        for(TableColumn<Devices, ?> tc:twDevices.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twDevices.setItems(devicesList);

        names=new String[]{"card", "name", "date", "number", "type", "oldDepartment","newDepartment","cost","oldMRP","newMRP"};
        i=0;
        for(TableColumn<Movements, ?> tc:twMovements.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twMovements.setItems(movementsList);

        names=new String[]{"card", "name", "number", "unit", "count", "mass"}; i=0;
        for(TableColumn<NobleMetals, ?> tc:twNobleMetals.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twNobleMetals.setItems(metalsList);

        names=new String[]{"card", "type", "name", "date", "number", "cost"}; i=0;
        for(TableColumn<RepairCost, ?> tc:twRepairCost.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twRepairCost.setItems(repairCostList);

        names=new String[]{"card", "date", "coefficient", "cost"}; i=0;
        for(TableColumn<Revaluation, ?> tc:twRevaluation.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twRevaluation.setItems(revaluationList);

        names=new String[]{"id", "recipient", "contacts", "basis", "name", "number", "date", "cost"}; i=0;
        for(TableColumn<Transfers, ?> tc:twTransfer.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twTransfer.setItems(transferList);

        names=new String[]{"id", "productionDate", "repairDate", "name", "number", "date", "period","depreciation", "residualCost"}; i=0;
        for(TableColumn<Transmission, ?> tc:twTransmisiion.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }

        twTransmisiion.setItems(transmissionList);

        StageAndController sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editCostChanges.fxml"), "Изменения стоимости");
        editCostChangeStage=sac.getStage();
        editCostChangesController=(EditCostChangesController) sac.getController();
        editCostChangesController.setStage(editCostChangeStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editDepreciation.fxml"), "Амортизация");
        editDepreciationStage=sac.getStage();
        editDepreciationController=(EditDepreciationController) sac.getController();
        editDepreciationController.setStage(editDepreciationStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editDevices.fxml"), "Устройстваа и принадлежности");
        editDevicesStage=sac.getStage();
        editDevicesController=(EditDevicesController) sac.getController();
        editDevicesController.setStage(editDevicesStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editMetals.fxml"), "Металлы");
        editMetalsStage=sac.getStage();
        editMetalsController=(EditMetalsController) sac.getController();
        editMetalsController.setStage(editMetalsStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editRepairCost.fxml"), "Ремонты");
        editRepaitCostStage=sac.getStage();
        editRepairCostsController =(EditRepairCostsController) sac.getController();
        editRepairCostsController.setStage(editRepaitCostStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editRevaluation.fxml"), "Переоценка");
        editRevaluationStage=sac.getStage();
        editRevaluationController=(EditRevaluationController) sac.getController();
        editRevaluationController.setStage(editRevaluationStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editTransfers.fxml"), "Передача другой организации");
        editTransfersStage=sac.getStage();
        editTransfersController=(EditTransfersController) sac.getController();
        editTransfersController.setStage(editTransfersStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editTransmission.fxml"), "Приём от другой организации");
        editTransmissionStage=sac.getStage();
        editTransmissionController=(EditTransmissionController) sac.getController();
        editTransmissionController.setStage(editTransmissionStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editMovements.fxml"), "Внутренние перемещения");
        editMovementsStage=sac.getStage();
        editMovementsController=(EditMovementsController) sac.getController();
        editMovementsController.setStage(editMovementsStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editInventoryCard.fxml"), "Инвентарная карточка");
        editInventoryCardStage=sac.getStage();
        editInventoryCardController=(EditInventoryCardController) sac.getController();
        editInventoryCardController.setStage(editInventoryCardStage);

    }

    @FXML
    void cbCardAction(ActionEvent event)
    {
        if(cbCard.getSelectionModel().isEmpty()) return;
        currendCardID=cbCard.getSelectionModel().getSelectedItem().getKey();

        updateCurrentTab();
    }

    @FXML
    void cbTableAction(ActionEvent event)
    {
        if(cbTable.getSelectionModel().isEmpty()) return;
        currentTabID=cbTable.getSelectionModel().getSelectedItem().getKey();
        updateCurrentTab();
        tpTabPane.getSelectionModel().select(currentTabID);
    }

    @FXML
    void btnCloseAction(ActionEvent event)
    {
        stage.close();
    }

    @FXML
    void btnEditMainTableAction(ActionEvent event)
    {
        if(twMainTable.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editInventoryCardController.setParameters(EditTypes.EDITING, twMainTable.getSelectionModel().getSelectedItem());
        editInventoryCardStage.showAndWait();
        if(editInventoryCardController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelMainTableAction(ActionEvent event)
    {
        if(twMainTable.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editInventoryCardController.setParameters(EditTypes.DELETING, twMainTable.getSelectionModel().getSelectedItem());
        editInventoryCardStage.showAndWait();
        if(editInventoryCardController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnShowInfoMainTableAction(ActionEvent event)
    {
        if(twMainTable.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editInventoryCardController.setParameters(EditTypes.VIEW, twMainTable.getSelectionModel().getSelectedItem());
        editInventoryCardStage.showAndWait();
    }

    @FXML
    void btnAddCostChangesAction(ActionEvent event)
    {
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }

        editCostChangesController.setParameters(EditTypes.ADDING, null);
        editCostChangeStage.showAndWait();
        if(editCostChangesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }
    @FXML
    void btnEditCostChangesAction(ActionEvent event)
    {
        if(twCostChanges.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }

        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editCostChangesController.setParameters(EditTypes.EDITING, twCostChanges.getSelectionModel().getSelectedItem());
        editCostChangeStage.showAndWait();
        if(editCostChangesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelCostChangesAction(ActionEvent event)
    {
        if(twCostChanges.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }

        editCostChangesController.setParameters(EditTypes.DELETING, twCostChanges.getSelectionModel().getSelectedItem());
        editCostChangeStage.showAndWait();
        if(editCostChangesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnEditDepreciationAction(ActionEvent event)
    {
        if(twDepreciation.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editDepreciationController.setParameters(EditTypes.EDITING,twDepreciation.getSelectionModel().getSelectedItem());
        editDepreciationStage.showAndWait();

        if(editDepreciationController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelDepreciationAction(ActionEvent event)
    {
        if(twDepreciation.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editDepreciationController.setParameters(EditTypes.DELETING,twDepreciation.getSelectionModel().getSelectedItem());
        editDepreciationStage.showAndWait();

        if(editDepreciationController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnShowInfoDepreciationAction(ActionEvent event)
    {
        if(twDepreciation.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editDepreciationController.setParameters(EditTypes.VIEW,twDepreciation.getSelectionModel().getSelectedItem());
        editDepreciationStage.showAndWait();
    }

    @FXML
    void btnAddDeviceAction(ActionEvent event)
    {
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editDevicesController.setParameters(EditTypes.ADDING, null);
        editDevicesStage.showAndWait();
        if(editDevicesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }
    @FXML
    void btnEditDevicaAction(ActionEvent event)
    {

        if(twDevices.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editDevicesController.setParameters(EditTypes.EDITING, twDevices.getSelectionModel().getSelectedItem());
        editDevicesStage.showAndWait();
        if(editDevicesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelDeviceAction(ActionEvent event)
    {

        if(twDevices.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editDevicesController.setParameters(EditTypes.DELETING, twDevices.getSelectionModel().getSelectedItem());
        editDevicesStage.showAndWait();
        if(editDevicesController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnEditMovementsAction(ActionEvent event)
    {

        if(twMovements.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editMovementsController.setParameters(EditTypes.EDITING, twMovements.getSelectionModel().getSelectedItem());
        editMovementsStage.showAndWait();
        if(editMovementsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }


    @FXML
    void btnShowInfoMovementsAction(ActionEvent event)
    {
        if(twMovements.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editMovementsController.setParameters(EditTypes.VIEW, twMovements.getSelectionModel().getSelectedItem());
        editMovementsStage.showAndWait();
    }

    @FXML
    void btnAddMetalsAction(ActionEvent event)
    {
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editMetalsController.setParameters(EditTypes.ADDING, null);
        editMetalsStage.showAndWait();
        if(editMetalsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnEditMetalsAction(ActionEvent event)
    {
        if(twNobleMetals.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editMetalsController.setParameters(EditTypes.EDITING, twNobleMetals.getSelectionModel().getSelectedItem());
        editMetalsStage.showAndWait();
        if(editMetalsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelMetalsAction(ActionEvent event)
    {
        if(twNobleMetals.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editMetalsController.setParameters(EditTypes.DELETING, twNobleMetals.getSelectionModel().getSelectedItem());
        editMetalsStage.showAndWait();
        if(editMetalsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }


    @FXML
    void btnEditRepaitCostAction(ActionEvent event)
    {

        if(twRepairCost.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editRepairCostsController.setParameters(EditTypes.EDITING, twRepairCost.getSelectionModel().getSelectedItem());
        editRepaitCostStage.showAndWait();
        if(editRepairCostsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelRepairCostAction(ActionEvent event)
    {
        if(twRepairCost.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editRepairCostsController.setParameters(EditTypes.DELETING, twRepairCost.getSelectionModel().getSelectedItem());
        editRepaitCostStage.showAndWait();
        if(editRepairCostsController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }


    @FXML
    void btnAddRevaluationAction(ActionEvent event)
    {
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editRevaluationController.setParameters(EditTypes.ADDING, null);
        editRevaluationStage.showAndWait();
        if(editRevaluationController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }
    @FXML
    void btnEditRevaluationAction(ActionEvent event)
    {
        if(twRevaluation.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editRevaluationController.setParameters(EditTypes.EDITING, twRevaluation.getSelectionModel().getSelectedItem());
        editRevaluationStage.showAndWait();
        if(editRevaluationController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnDelRevaluationAction(ActionEvent event)
    {
        if(twRevaluation.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        if(currendCardID!=-1 && CardChecker.checkCardWriteoff(currendCardID))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Невозможно совершить операцию, " +
                    "поскольку данная карточка списана с учёта");
            return;
        }
        editRevaluationController.setParameters(EditTypes.DELETING, twRevaluation.getSelectionModel().getSelectedItem());
        editRevaluationStage.showAndWait();
        if(editRevaluationController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnEditTransferAction(ActionEvent event)
    {
        if(twTransfer.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editTransfersController.setParameters(EditTypes.EDITING, twTransfer.getSelectionModel().getSelectedItem());
        editTransfersStage.showAndWait();
        if(editTransfersController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnShowInfoTransferAction(ActionEvent event)
    {
        if(twTransfer.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editTransfersController.setParameters(EditTypes.VIEW, twTransfer.getSelectionModel().getSelectedItem());
        editTransfersStage.showAndWait();
    }

    @FXML
    void btnEditTransmissionAction(ActionEvent event)
    {
        if(twTransmisiion.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editTransmissionController.setParameters(EditTypes.EDITING, twTransmisiion.getSelectionModel().getSelectedItem());
        editTransmissionStage.showAndWait();
        if(editTransmissionController.getResult()==ResultTypes.SUCCESS)
            updateCurrentTab();
    }

    @FXML
    void btnShowInfoTransmisiion(ActionEvent event)
    {
        if(twTransmisiion.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editTransmissionController.setParameters(EditTypes.VIEW, twTransmisiion.getSelectionModel().getSelectedItem());
        editTransmissionStage.showAndWait();
    }

    public void setStage(Stage stage)
    {
        this.stage =stage;

        this.stage.setOnShown(event ->
        {

            StackPane sp= (StackPane) tpTabPane.lookup(".tab-header-area");
            sp.setVisible(false);

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getCardListFromDBTable());
            cbCard.getItems().add(0, new MyKeyValue(-1, "Все карточки"));

            cbCard.getSelectionModel().select(0);
            cbTable.getSelectionModel().select(0);
        });
    }

    private void tab1Show()
    {
        mainTableList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from inventory_card";
        else
            query="select * from inventory_card where id_inventory_card="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                mainTableList.add(new InventoryCard(rs.getInt("id_inventory_card"),
                        rs.getInt("card_number"), rs.getString("organization_name"),
                        rs.getInt("department"), rs.getDate("card_date"),
                        rs.getString("object_name"), rs.getString("OKYD"),
                        rs.getString("OKPO"),rs.getString("OKOF"),
                        rs.getInt("depreciation_group_number"), rs.getString("passport_number"),
                        rs.getString("serial_number"), rs.getString("inventory_number"),
                        rs.getDate("delivery_date"),
                        rs.getDate("writeoff_date"),
                        rs.getString("account_number"),rs.getString("location"),
                        rs.getString("department_number"),rs.getString("manufacturer"),
                        rs.getDouble("initial_cost"), rs.getInt("useful_life"),
                        rs.getInt("person_responsible_for_card"),
                        rs.getInt("MRP"), rs.getString("participants"),
                        rs.getInt("ownership_share"), rs.getString("name_of_structural_elements_etc"),
                        rs.getString("characteristics_main_object"),rs.getString("characteristics_of_the_devices_etc"),
                        rs.getString("notice")
                        ));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }


    }
    private void tab2Show()
    {
        costChangesList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from changes_in_cost";
        else
            query="select * from changes_in_cost where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                costChangesList.add(new CostChanges(rs.getInt("id_changes_in_cost"),
                        rs.getInt("card_number"), rs.getInt("operation_type"),
                        rs.getString("document_name"), rs.getDate("document_data"),
                        rs.getString("document_number"), rs.getDouble("amount_of_expenses")));
            }
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab3Show()
    {
        depreciationList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from depreciation";
        else
            query="select * from depreciation where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                depreciationList.add(new Depreciation(rs.getInt("id_depreciation"),
                        rs.getInt("card_number"), rs.getInt("year"),
                         rs.getDouble("janyary"),rs.getDouble("february"),
                        rs.getDouble("march"),rs.getDouble("april"),
                        rs.getDouble("may"),rs.getDouble("june"),
                        rs.getDouble("july"),rs.getDouble("august"),
                        rs.getDouble("september"),rs.getDouble("october"),
                        rs.getDouble("november"),rs.getDouble("december")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab4Show()
    {
        devicesList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from devices";
        else
            query="select * from devices where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                devicesList.add(new Devices(rs.getInt("id_devices"),
                        rs.getInt("card_number"), rs.getString("device_name"),
                        rs.getInt("count")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab5Show()
    {
        movementsList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from movements";
        else
            query="select * from movements where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                movementsList.add(new Movements(rs.getInt("id_movements"),
                        rs.getInt("card_number"), rs.getString("document_name"),
                        rs.getDate("document_data"), rs.getString("document_number"),
                        rs.getInt("operation_type"), rs.getInt("old_department"),
                        rs.getInt("new_department"), rs.getDouble("residual_cost"),
                        rs.getInt("old_mrp"), rs.getInt("new_mrp")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab6Show()
    {
        metalsList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from noble_metals";
        else
            query="select * from noble_metals where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                metalsList.add(new NobleMetals(rs.getInt("id_noble_metals"),
                        rs.getInt("card_number"), rs.getString("name_metal"),
                        rs.getString("number"), rs.getString("unit"),
                        rs.getInt("count"), rs.getInt("mass")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab7Show()
    {
        repairCostList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from repair_costs";
        else
            query="select * from repair_costs where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                repairCostList.add(new RepairCost(rs.getInt("id_repair_costs"),
                        rs.getInt("card_number"), rs.getInt("repair_type"),
                        rs.getString("document_name"), rs.getDate("document_data"),
                        rs.getString("document_number"), rs.getInt("amount_of_expenses")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab8Show()
    {
        revaluationList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from revaluation";
        else
            query="select * from revaluation where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                revaluationList.add(new Revaluation(rs.getInt("id_revaluation"),
                        rs.getInt("card_number"), rs.getDate("date"),
                        rs.getDouble("coefficient"), rs.getDouble("replacement_cost")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab9Show()
    {
        transferList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from transfers";
        else
            query="select * from transfers where id_transfers="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                transferList.add(new Transfers(rs.getInt("id_transfers"),
                        rs.getString("recipient"), rs.getString("contact_info"),
                        rs.getString("basis"), rs.getString("document_name"),
                        rs.getString("document_number"), rs.getDate("document_date"),
                        rs.getDouble("cost")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void tab10Show()
    {
        transmissionList.clear();

        String query;

        if(currendCardID==-1)
            query="select * from transmission_info";
        else
            query="select * from transmission_info where id_transmission_info="+cbCard.getSelectionModel().getSelectedItem().getKey();

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(query);
            while (rs.next())
            {
                transmissionList.add(new Transmission(rs.getInt("id_transmission_info"),
                        rs.getDate("production_date"),
                        rs.getDate("repair_date"), rs.getString("document_name"),
                        rs.getString("document_number"), rs.getDate("document_date"),
                        rs.getInt("operation_period"), rs.getDouble("depreciation_amount"),
                        rs.getDouble("residual_cost")));
            }

        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
            }

        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }
    private void updateCurrentTab()
    {
        switch (currentTabID)
        {
            case 0: tab1Show();break;
            case 1: tab2Show();break;
            case 2: tab3Show();break;
            case 3: tab4Show();break;
            case 4: tab5Show();break;
            case 5: tab6Show();break;
            case 6: tab7Show();break;
            case 7: tab8Show();break;
            case 8: tab9Show();break;
            case 9: tab10Show();break;
        }
    }

}
