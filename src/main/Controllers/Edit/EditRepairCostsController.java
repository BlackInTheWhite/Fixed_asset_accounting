package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.RepairCost;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditRepairCostsController extends EditController<RepairCost> {

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
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) должны быть заполнены");
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


    @Override
    protected void addObject()
    {

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите добавить затраты на ремонт?"))
            return;


        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("insert into repair_costs values (?,?,?,?,?,?,?)");
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
            AlertWindows.showInfoAlert("Операция завершена",null,"Затраты на ремонт успешно добавлены");
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

    @Override
    protected void editObject()
    {

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите отредактировать запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement  ps=connection.prepareStatement("update repair_costs set card_number=?, " +
                    "repair_type=?, document_name=?, document_data=?, document_number=?, " +
                    "amount_of_expenses=? where id_repair_costs=?");
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
            AlertWindows.showInfoAlert("Операция завершена",null,"Запись отредактирована");

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

    @Override
    protected void delObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate("delete from repair_costs where id_repair_costs="+getCurrentObject().getId());

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

    public void setParameters(int edit, RepairCost object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfDocumentName, tfAmount, tfDocumentNumber);
            FieldCleaner.clear(dpDocumentDate);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
            cbOperation.setItems(Fixed_asset_accounting.getMyDatabase().getListFromDBTable("repair_type_directory", "id_repair_type", "type_name"));
            stage.setTitle("Затраты на ремонт");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getCard()));
            cbCard.getSelectionModel().select(0);

            cbOperation.setItems(Fixed_asset_accounting.getMyDatabase().getOneOperationAsList("repair_type_directory",
                    "id_repair_type","type_name", getCurrentObject().getType()));
            cbOperation.getSelectionModel().select(0);

            tfDocumentName.setText(getCurrentObject().getName());
            tfDocumentNumber.setText(getCurrentObject().getNumber());
            tfAmount.setText(String.valueOf(getCurrentObject().getCost()));
            dpDocumentDate.setValue(getCurrentObject().getDate().toLocalDate());

            if(edit==EditTypes.EDITING)
            {
                stage.setTitle("Редактирование записи");
                btnSave.setText("Редактировать");
            }
            else if(edit==EditTypes.DELETING)
            {
                stage.setTitle("Удаление записи");
                btnSave.setText("Удалить");
            }
            else
                stage.setTitle("Просмотр записи");
        }
        tfAmount.setEditable(editable);
        tfDocumentName.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        dpDocumentDate.setEditable(editable);

        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

}
