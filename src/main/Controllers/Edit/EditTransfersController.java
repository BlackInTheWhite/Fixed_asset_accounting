package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Transfers;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditTransfersController extends EditController<Transfers>
{
    @FXML
    private TextField tfRecipient, tfDocumentName, tfContactInfo, tfAmount, tfDocumentNumber, tfBasis;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<MyKeyValue> cbCard;

    @FXML
    private DatePicker dpDocumentDate, dpWriteOffDate;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfAmount);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfRecipient, tfDocumentName,
                tfContactInfo, tfDocumentNumber, tfBasis);

        dpDocumentDate.setOnMouseClicked(event ->
        {
            if(!dpDocumentDate.isEditable())
                dpDocumentDate.hide();
        });

        dpWriteOffDate.setEditable(false);
        dpWriteOffDate.setOnMouseClicked(event ->
        {
            if(!dpWriteOffDate.isEditable())
                dpWriteOffDate.hide();
        });
    }

    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfRecipient, tfDocumentName, tfContactInfo, tfAmount, tfDocumentNumber, tfBasis);
        check|=EmptyChecker.isAnyEmpty(dpDocumentDate, dpWriteOffDate);
        check|=EmptyChecker.isAnyEmpty(cbCard);
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
    }

    public void setParameters(int edit, Transfers object)
    {
        super.setParameters(edit, object);
        boolean editable=false;

        dpDocumentDate.setEditable(false);


        if(edit== EditTypes.ADDING)
        {
            editable=true;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
            stage.setTitle("Передача объекта другой организации");
            FieldCleaner.clear(tfRecipient, tfDocumentName, tfContactInfo, tfAmount, tfDocumentNumber, tfBasis);
            FieldCleaner.clear(cbCard);
            FieldCleaner.clear(dpDocumentDate, dpWriteOffDate);

            dpDocumentDate.setEditable(true);

        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.VIEW)
        {
            editable=(edit==EditTypes.EDITING);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getId()));
            cbCard.getSelectionModel().select(0);
            stage.setTitle("Редактирование записи");

            tfRecipient.setText(getCurrentObject().getRecipient());
            tfContactInfo.setText(getCurrentObject().getContacts());
            tfBasis.setText(getCurrentObject().getBasis());
            tfDocumentName.setText(getCurrentObject().getName());
            tfDocumentNumber.setText(getCurrentObject().getNumber());
            tfAmount.setText(String.valueOf(getCurrentObject().getCost()));
            dpDocumentDate.setValue(getCurrentObject().getDate().toLocalDate());
            dpWriteOffDate.setValue(Fixed_asset_accounting.getMyDatabase().getCardWriteOffDate(getCurrentObject().getId()).toLocalDate());
            if(!editable)
            {
                btnSave.setVisible(false);
                stage.setTitle("Просмотр записи");
            }

        }

        tfRecipient.setEditable(editable);
        tfContactInfo.setEditable(editable);
        tfBasis.setEditable(editable);
        tfDocumentName.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        tfAmount.setEditable(editable);
        dpDocumentDate.setEditable(editable);
        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите совершить операцию по передаче объекта?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            int i=1;
            PreparedStatement ps=connection.prepareStatement("insert into transfers values(?,?,?,?,?,?,?,?)");

            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfRecipient.getText());
            ps.setString(i++, tfContactInfo.getText());
            ps.setString(i++, tfBasis.getText());
            ps.setString(i++, tfDocumentName.getText());
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfAmount.getText())));

            ps.execute();
            ps.close();

            PreparedStatement ps1=connection.prepareStatement("update inventory_card set writeoff_date=? where id_inventory_card=?");

            ps1.setDate(1,Date.valueOf(dpWriteOffDate.getValue()));
            ps1.setInt(2, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps1.execute();
            ps1.close();

            connection.commit();
            AlertWindows.showInfoAlert(null,null,"Передача объекта успешно выполнена");
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

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            int i=1;
            PreparedStatement ps=connection.prepareStatement("update transfers set " +
                    "recipient=?, contact_info=?, basis=?, " +
                    "document_name=?, document_number=?, document_date=?," +
                    "cost=? where id_transfers=?");

            ps.setString(i++, tfRecipient.getText());
            ps.setString(i++, tfContactInfo.getText());
            ps.setString(i++, tfBasis.getText());
            ps.setString(i++, tfDocumentName.getText());
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfAmount.getText())));
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Запись успешно отредактирована");
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
