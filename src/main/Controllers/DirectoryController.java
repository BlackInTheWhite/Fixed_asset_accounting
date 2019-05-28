package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Controllers.Edit.EditGroupController;
import main.Controllers.Edit.EditRPController;
import main.Controllers.Edit.EditValueController;
import main.Fixed_asset_accounting;
import main.others.Tables.DepreciationGroup;
import main.others.Tables.ResponsiblePerson;
import main.others.Utils.*;

import java.sql.*;

/**
 * Created by User on 05.06.2018.
 */
public class DirectoryController
{
    @FXML
    private TabPane tpTest;

    @FXML
    private TableView<DepreciationGroup> twDepreciationGroup;

    @FXML
    private TableView<ResponsiblePerson> twRP;

    @FXML
    private TableView<MyType> twType;

    @FXML
    private ComboBox<MyKeyValueEx> cbTable;

    private Stage stage, editRPStage, editValueStage, editGroupStage;
    private String currentTable;
    private String currentIDColumn;
    private ObservableList<ResponsiblePerson> responsiblePeople=FXCollections.observableArrayList();
    private ObservableList<MyType> types=FXCollections.observableArrayList();
    private ObservableList<DepreciationGroup> groups=FXCollections.observableArrayList();
    private EditRPController editRPController;
    private EditGroupController editGroupController;
    private EditValueController editValueController;

    @FXML
    public void initialize()
    {
        ObservableList<MyKeyValueEx> list=FXCollections.observableArrayList(new MyKeyValueEx(0,"Лица, ответственные за карточку","crp", "id_crp"),
                new MyKeyValueEx(0,"Материально-ответственные лица", "mrp", "id_mrp"), new MyKeyValueEx(1,"Виды изменения стоимости","changes_cost_operation_type_directory", "id_operation_type"),
                new MyKeyValueEx(1,"Структурные подразделения", "department_directory", "id_department_directory"), new MyKeyValueEx(1,"Виды внутренних перемещений", "operation_type_movements_directory", "id_operation_type_movements"),
                new MyKeyValueEx(1,"Виды ремонтов", "repair_type_directory", "id_repair_type"), new MyKeyValueEx(2, "Группы амортизации", "depreciation_group_directory", "id_depreciation_group_directory"));
        cbTable.setItems(list);
        String[] names={"surname", "name", "patronymic", "position", "number"};
        int i=0;
        for(TableColumn<ResponsiblePerson, ?> tc:twRP.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }
        twRP.setItems(responsiblePeople);

        names=new String[]{"number", "name", "characteristic"};
        i=0;
        for(TableColumn<DepreciationGroup, ?> tc:twDepreciationGroup.getColumns())
        {
            tc.setCellValueFactory(new PropertyValueFactory<>(names[i++]));
        }
        twDepreciationGroup.setItems(groups);

        twType.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("type"));
        twType.setItems(types);

        StageAndController sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editRP.fxml"), "Ответственное лицо");
        editRPStage=sac.getStage();
        editRPController=(EditRPController)sac.getController();
        editRPController.setStage(editRPStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editGroup.fxml"), "Амортизационная группа");
        editGroupStage=sac.getStage();
        editGroupController=(EditGroupController)sac.getController();
        editGroupController.setStage(editGroupStage);

        sac= StageAndControllerInitializer.defaultStageInit(getClass().getResource("/main/fxml/Edit/editValue.fxml"), "");
        editValueStage=sac.getStage();
        editValueController=(EditValueController)sac.getController();
        editValueController.setStage(editValueStage);

    }

    public void setStage(Stage stage)
    {
        this.stage =stage;

        this.stage.setOnShown(event -> {

            StackPane sp= (StackPane) tpTest.lookup(".tab-header-area");
            sp.setVisible(false);
            cbTable.getSelectionModel().select(0);
        });
    }

    @FXML
    void btnAddRPAction(ActionEvent event)
    {
        editRPController.setParameters(EditTypes.ADDING, null, currentTable, null);
        editRPStage.showAndWait();
        if(editRPController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnEditRPAction(ActionEvent event)
    {
        if(twRP.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }

        MyKeyValueEx item=cbTable.getSelectionModel().getSelectedItem();
        editRPController.setParameters(EditTypes.EDITING, twRP.getSelectionModel().getSelectedItem(),
                item.getTable(), item.getIdColumn());
        editRPStage.showAndWait();

        if(editRPController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnDelRPAction(ActionEvent event)
    {
        if(twRP.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        MyKeyValueEx item=cbTable.getSelectionModel().getSelectedItem();
        editRPController.setParameters(EditTypes.DELETING, twRP.getSelectionModel().getSelectedItem(),
                item.getTable(), item.getIdColumn());

        editRPStage.showAndWait();
        if(editRPController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnAddTypeAction(ActionEvent event)
    {

        String insertColumn=null;
        switch (cbTable.getSelectionModel().getSelectedItem().getTable())
        {
            case "changes_cost_operation_type_directory": insertColumn="operation_type_name"; break;
            case "department_directory": insertColumn="department_name";break;
            case "operation_type_movements_directory": insertColumn="operation_type_name";break;
            case "repair_type_directory": insertColumn="type_name";break;
        }


        editValueController.setParameters(EditTypes.ADDING, null,cbTable.getSelectionModel().getSelectedItem().getTable(),
                cbTable.getSelectionModel().getSelectedItem().getIdColumn(), insertColumn);
        editValueStage.showAndWait();
        if(editValueController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnEditTypeAction(ActionEvent event)
    {
        if(twType.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        String insertColumn=null;
        switch (cbTable.getSelectionModel().getSelectedItem().getTable())
        {
            case "changes_cost_operation_type_directory": insertColumn="operation_type_name"; break;
            case "department_directory": insertColumn="department_name";break;
            case "operation_type_movements_directory": insertColumn="operation_type_name";break;
            case "repair_type_directory": insertColumn="type_name";break;
        }
        editValueController.setParameters(EditTypes.EDITING,
                twType.getSelectionModel().getSelectedItem(),
                cbTable.getSelectionModel().getSelectedItem().getTable(),
                cbTable.getSelectionModel().getSelectedItem().getIdColumn(), insertColumn);

        editValueStage.showAndWait();

        if(editValueController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnDelTypeAction(ActionEvent event)
    {
        if(twType.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }


        editValueController.setParameters(EditTypes.DELETING,
                twType.getSelectionModel().getSelectedItem(),
                cbTable.getSelectionModel().getSelectedItem().getTable(),
                cbTable.getSelectionModel().getSelectedItem().getIdColumn(), null);

        editValueStage.showAndWait();

        if(editValueController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnAddDepreciationGroupAction(ActionEvent event)
    {
        editGroupController.setParameters(EditTypes.ADDING, null);
        editGroupStage.showAndWait();
        if(editGroupController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);
    }

    @FXML
    void btnEditDepreciationGroupAction(ActionEvent event)
    {
        if(twDepreciationGroup.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }
        editGroupController.setParameters(EditTypes.EDITING, twDepreciationGroup.getSelectionModel().getSelectedItem());
        editGroupStage.showAndWait();
        if(editGroupController.getResult()==ResultTypes.SUCCESS)
        cbTableAction(null);
    }

    @FXML
    void btnDelDepreciationGroupAction(ActionEvent event)
    {
        if(twDepreciationGroup.getSelectionModel().getSelectedIndex()==-1)
        {
            AlertWindows.showErrorAlert("Запись не выбрана", null, "Выберите запись в таблице");
            return;
        }

        editGroupController.setParameters(EditTypes.DELETING, twDepreciationGroup.getSelectionModel().getSelectedItem());
        editGroupStage.showAndWait();
        if(editGroupController.getResult()==ResultTypes.SUCCESS)
            cbTableAction(null);

    }

    @FXML
    void btnCloseAction(ActionEvent event) {
        stage.close();
    }

    public void cbTableAction(ActionEvent actionEvent)
    {
        if(cbTable.getSelectionModel().isEmpty())
        {
            currentTable=null;
            return;
        }

        currentTable=cbTable.getSelectionModel().getSelectedItem().getTable();
        currentIDColumn=cbTable.getSelectionModel().getSelectedItem().getIdColumn();
        tpTest.getSelectionModel().select(cbTable.getSelectionModel().getSelectedItem().getKey());

        switch (tpTest.getSelectionModel().getSelectedIndex())
        {
            case 0: firstTabShown();break;
            case 1: secondTabShown();break;
            case 2: thirdTabShown();break;
        }

    }

    private void firstTabShown()
    {
        responsiblePeople.clear();

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from "+currentTable);
            while (rs.next())
            {
                responsiblePeople.add(new ResponsiblePerson(rs.getInt(currentIDColumn), rs.getString("surname"),
                        rs.getString("name"), rs.getString("patronymic"),
                        rs.getString("position"), rs.getString("personnel_number")));
            }
            connection.commit();
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

    private void secondTabShown()
    {
        types.clear();

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from "+currentTable);
            while (rs.next())
            {
                types.add(new MyType(rs.getInt(1), rs.getString(2)));
            }
            connection.commit();
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

    private void thirdTabShown()
    {
        groups.clear();

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from "+currentTable);
            while (rs.next())
            {
                groups.add(new DepreciationGroup(rs.getInt("id_depreciation_group_directory"),
                        rs.getString("group_number"),
                        rs.getString("group_name"), rs.getString("characteristic")));
            }
            connection.commit();
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

}
