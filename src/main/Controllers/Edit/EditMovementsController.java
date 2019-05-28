package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Movements;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by User on 03.06.2018.
 */
public class EditMovementsController extends EditController<Movements>
{
    @FXML
    private ComboBox<MyKeyValue> cbCard,cbNewMRP, cbNewDepartment;


    @FXML
    private TextField tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldDepartment, tfOldMRP;

    @FXML
    private DatePicker dpDocumentDate;
    @FXML
    private Button btnSave;

    private Stage stage;

    private MyKeyValue oldDepartment, oldMRP;
    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfDocumentName, tfDocumentNumber);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfResidualCost);

        dpDocumentDate.setOnMouseClicked(event ->
        {
            if(!dpDocumentDate.isEditable())
                dpDocumentDate.hide();
        });

    }

    public void btnSaveAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(cbCard, cbNewMRP, cbNewDepartment);
        check|=EmptyChecker.isAnyEmpty(tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldDepartment, tfOldMRP);
        check|=EmptyChecker.isAnyEmpty(dpDocumentDate);

        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) не должны быть пустыми.");
            return;
        }

        if(cbNewDepartment.getSelectionModel().getSelectedItem().toString().equals(tfOldDepartment.getText()) &&
                cbNewMRP.getSelectionModel().getSelectedItem().toString().equals(tfOldMRP.getText()))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Данная операция не имеет смысла.");
            return;
        }

        doOperation();
    }

    public void btnCancelAction(ActionEvent actionEvent)
    {
        setResult(ResultTypes.CANCEL);
        stage.close();
    }

    public void setStage(Stage stage)
    {
        this.stage =stage;
        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
    }

    public void cbCardAction(ActionEvent actionEvent)
    {
        if(cbCard.getSelectionModel().isEmpty()) return;
        oldDepartment=Fixed_asset_accounting.getMyDatabase().getCardDepartment(cbCard.getSelectionModel().getSelectedItem().getKey());
        oldMRP=Fixed_asset_accounting.getMyDatabase().getCardMRP(cbCard.getSelectionModel().getSelectedItem().getKey());
        tfOldDepartment.setText(oldDepartment.getValue());
        tfOldMRP.setText(oldMRP.getValue());
    }

    public void setParameters(int edit, Movements object)
    {
        super.setParameters(edit, object);
        boolean editable=false;

        if(edit== EditTypes.ADDING)
        {
            editable=true;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
            cbNewDepartment.setItems(Fixed_asset_accounting.getMyDatabase().getDepartmentList());
            cbNewMRP.setItems(Fixed_asset_accounting.getMyDatabase().getResponsiblePersonList("mrp"));
            stage.setTitle("Внутреннее перемещение");
            FieldCleaner.clear(tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldMRP, tfOldDepartment);
            FieldCleaner.clear(cbCard, cbNewMRP, cbNewDepartment);
            FieldCleaner.clear(dpDocumentDate);
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.VIEW)
        {
            editable=(edit==EditTypes.EDITING);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(object.getCard()));
            cbCard.getSelectionModel().select(0);

            tfOldDepartment.setText(Fixed_asset_accounting.getMyDatabase().getDepartmentByID(getCurrentObject().getOldDepartment()));
            tfOldMRP.setText(Fixed_asset_accounting.getMyDatabase().getMRPByID(getCurrentObject().getOldMRP()));

            cbNewDepartment.setItems(Fixed_asset_accounting.getMyDatabase().getOneDepartmentAsList(object.getNewDepartment()));
            cbNewDepartment.getSelectionModel().select(0);

            cbNewMRP.setItems(Fixed_asset_accounting.getMyDatabase().getOneRPAsList("mrp", "id_mrp", object.getNewMRP()));
            cbNewMRP.getSelectionModel().select(0);

            tfDocumentName.setText(object.getName());
            dpDocumentDate.setValue(object.getDate().toLocalDate());
            tfDocumentNumber.setText(object.getNumber());
            tfResidualCost.setText(String.valueOf(object.getCost()));
            stage.setTitle("Редактирование записи");
            btnSave.setText("Редактировать");

            if(!editable)
            {
                btnSave.setVisible(false);
                stage.setTitle("Просмотр записи");
            }

        }
        tfDocumentName.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        tfResidualCost.setEditable(editable);
        dpDocumentDate.setEditable(editable);

        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите совершить операцию?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();

        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("insert into movements(card_number, document_name, " +
                    "document_data, document_number, operation_type, residual_cost, old_department, new_department, " +
                    "old_mrp, new_mrp) values(?,?,?,?,?,?,?,?,?,?)");
            Statement st=connection.createStatement();
            ResultSet rs=st.executeQuery("select id_operation_type_movements from operation_type_movements_directory" +
                    " where operation_type_name=\'Внутреннее перемещение\'");
            if(!rs.next())
            {
                st.executeUpdate("insert into operation_type_movements_directory(operation_type_name) values ('Внутренее перемещение')");
                rs=st.executeQuery("select id_operation_type_movements from operation_type_movements_directory" +
                        " where operation_type_name=\'Внутреннее перемещение\'");
            }
            int idType=rs.getInt("id_operation_type_movements");
            int i=1;
            ps.setInt(i++,cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfDocumentName.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setInt(i++, idType);
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfResidualCost.getText())));
            ps.setInt(i++, oldDepartment.getKey());
            ps.setInt(i++,cbNewDepartment.getSelectionModel().getSelectedItem().getKey());
            ps.setInt(i++, oldMRP.getKey());
            ps.setInt(i++,cbNewMRP.getSelectionModel().getSelectedItem().getKey());


            ps.execute();
            ps.close();

            st.executeUpdate("update inventory_card set department="+cbNewDepartment.getSelectionModel().getSelectedItem().getKey()+
                    ", MRP="+cbNewMRP.getSelectionModel().getSelectedItem().getKey()+
                    " where id_inventory_card="+cbCard.getSelectionModel().getSelectedItem().getKey());
            connection.commit();

            AlertWindows.showInfoAlert(null,null,"Запись успешно добавлена");
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
        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();

        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("update movements set document_name=?, " +
                    "document_data=?, document_number=?, residual_cost=? where id_movements=?");
            Statement st=connection.createStatement();

            int i=1;
            ps.setString(i++, tfDocumentName.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfResidualCost.getText())));
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();

            AlertWindows.showInfoAlert(null,null,"Запись успешно отредактирована");
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

    }
}
