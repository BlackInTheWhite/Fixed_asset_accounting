package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.CostChanges;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditCostChangesController extends EditController<CostChanges> {

    @FXML
    private ComboBox<MyKeyValue> cbOperation, cbCard;

    @FXML
    private TextField tfDocumentName, tfAmount, tfDocumentNumber;

    @FXML
    private DatePicker dpDocumentDate;

    @FXML
    private Button btnSave;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfDocumentName, tfDocumentNumber);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfAmount);

        dpDocumentDate.setOnMouseClicked(event ->
        {
            if(!dpDocumentDate.isEditable())
                dpDocumentDate.hide();
        });
    }

    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfDocumentName, tfAmount, tfDocumentNumber);
        check|=EmptyChecker.isAnyEmpty(dpDocumentDate);
        check|=EmptyChecker.isAnyEmpty(cbCard, cbOperation);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода", null, "Основные поля (*) должны быть заполнены");
            return;
        }

        doOperation();
    }

    @FXML
    void btnCloseAction(ActionEvent event)
    {
        setResult(ResultTypes.CANCEL);
        stage.close();
    }

    public void setStage(Stage stage)
    {
        this.stage=stage;
        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
    }


    public void setParameters(int edit, CostChanges object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfDocumentName, tfAmount, tfDocumentNumber);
            FieldCleaner.clear(dpDocumentDate);

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
            cbOperation.setItems(Fixed_asset_accounting.getMyDatabase().getListFromDBTable(
                    "changes_cost_operation_type_directory",
                    "id_operation_type", "operation_type_name"));

            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {

            editable=edit==EditTypes.EDITING;

            tfDocumentName.setText(getCurrentObject().getName());
            tfDocumentNumber.setText(getCurrentObject().getNumber());
            tfAmount.setText(String.valueOf(getCurrentObject().getCost()));
            dpDocumentDate.setValue(getCurrentObject().getDate().toLocalDate());

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getCard()));
            cbCard.getSelectionModel().select(0);

            cbOperation.setItems(Fixed_asset_accounting.getMyDatabase().getOneOperationAsList("changes_cost_operation_type_directory",
                    "id_operation_type", "operation_type_name", getCurrentObject().getType()));
            cbOperation.getSelectionModel().select(0);

            if(edit==EditTypes.EDITING)
            {
                stage.setTitle("Редактирование записи");
                btnSave.setText("Редактировать");
            }
            if (edit == EditTypes.DELETING) {
                stage.setTitle("Удаление записи");
                btnSave.setText("Удалить");
            } else {
                stage.setTitle("Просмотр записи");
            }

        }
        tfAmount.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        tfDocumentName.setEditable(editable);
        dpDocumentDate.setEditable(editable);

        btnSave.setVisible(!(edit==EditTypes.VIEW));

    }

    @Override
    protected void addObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите добавить запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("insert into changes_in_cost values (?,?,?,?,?,?,?)");
            int i=1;

            ps.setNull(i++, Types.INTEGER);
            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setInt(i++, cbOperation.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfDocumentName.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfAmount.getText())));

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена", null, "Запись успешно добавлена");
            setResult(ResultTypes.SUCCESS);
            stage.close();
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());

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

    @Override
    protected void editObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите отредактировать запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("update changes_in_cost set card_number=?, " +
                    "operation_type=?, document_name=?, document_data=?, document_number=?, " +
                    "amount_of_expenses=? where id_changes_in_cost=?");
            int i=1;

            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setInt(i++, cbOperation.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfDocumentName.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfAmount.getText())));
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена", null, "Запись успешно отредактирована");
            setResult(ResultTypes.SUCCESS);
            stage.close();
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());

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

    @Override
    protected void delObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить выбранную запись?"))
            return;
        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();

            statement.executeUpdate("delete from changes_in_cost where id_changes_in_cost="+getCurrentObject().getId());
            statement.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Запись успешно удалена");
            setResult(ResultTypes.SUCCESS);
            stage.close();

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
