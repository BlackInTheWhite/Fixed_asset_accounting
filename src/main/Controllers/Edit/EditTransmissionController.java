package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Transmission;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTransmissionController extends EditController<Transmission>{

    @FXML
    private TextField tfCost, tfDocumentName, tfDocumentNumber, tfDepreciation, tfPeriod;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker dpProductionDate, dpDocumentDate, dpRepairDate;


    @FXML
    private ComboBox<MyKeyValue> cbCard;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfCost, tfDepreciation);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfDocumentName, tfDocumentNumber);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfPeriod);

        dpProductionDate.setOnMouseClicked(event ->
        {
            if(!dpProductionDate.isEditable())
                dpProductionDate.hide();
        });
        dpDocumentDate.setOnMouseClicked(event ->
        {
            if(!dpDocumentDate.isEditable())
                dpDocumentDate.hide();
        });
        dpRepairDate.setOnMouseClicked(event ->
        {
            if(!dpRepairDate.isEditable())
                dpRepairDate.hide();
        });

    }

    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfCost, tfDocumentName, tfDocumentNumber, tfDepreciation, tfPeriod);
        check|=EmptyChecker.isAnyEmpty(dpDocumentDate, dpProductionDate, dpRepairDate);
        check|=EmptyChecker.isAnyEmpty(cbCard);
        if(check)
        {
            AlertWindows.showErrorAlert("Error", null, "Основные поля (*) должны быть заполнены");
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

    @Override
    protected void addObject() {

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
            PreparedStatement ps=connection.prepareStatement("update transmission_info set " +
                    "production_date=?, repair_date=?, document_name=?, " +
                    "document_number=?, document_date=?, operation_period=?," +
                    "depreciation_amount=?, residual_cost=? where id_transmission_info=?");

            ps.setDate(i++, Date.valueOf(dpProductionDate.getValue()));
            ps.setDate(i++, Date.valueOf(dpRepairDate.getValue()));
            ps.setString(i++, tfDocumentName.getText());
            ps.setString(i++, tfDocumentNumber.getText());
            ps.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
            ps.setInt(i++, Integer.parseInt(tfPeriod.getText()));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfDepreciation.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfCost.getText())));

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
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void delObject() {

    }

    public void setParameters(int edit, Transmission object)
    {
        super.setParameters(edit, object);
        boolean editable=false;

        dpDocumentDate.setEditable(false);

        if(edit== EditTypes.EDITING || edit==EditTypes.VIEW)
        {
            editable=(edit==EditTypes.EDITING);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getId()));
            cbCard.getSelectionModel().select(0);
            stage.setTitle("Редактирование записи");

            dpProductionDate.setValue(getCurrentObject().getProductionDate().toLocalDate());
            dpRepairDate.setValue(getCurrentObject().getRepairDate().toLocalDate());
            tfDocumentName.setText(getCurrentObject().getName());
            tfDocumentNumber.setText(getCurrentObject().getNumber());
            dpDocumentDate.setValue(getCurrentObject().getDate().toLocalDate());
            tfPeriod.setText(String.valueOf(getCurrentObject().getPeriod()));
            tfDepreciation.setText(String.valueOf(getCurrentObject().getDepreciation()));
            tfCost.setText(String.valueOf(getCurrentObject().getResidualCost()));

            if(!editable)
            {
                btnSave.setVisible(false);
                stage.setTitle("Просмотр записи");
            }

        }

        tfDocumentName.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        tfPeriod.setEditable(editable);
        tfDepreciation.setEditable(editable);
        tfDocumentNumber.setEditable(editable);
        tfCost.setEditable(editable);
        dpDocumentDate.setEditable(editable);
        dpProductionDate.setEditable(editable);
        dpRepairDate.setEditable(editable);
        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }


}
