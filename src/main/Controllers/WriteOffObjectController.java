package main.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by User on 03.06.2018.
 */
public class WriteOffObjectController
{
    @FXML
    private ComboBox<MyKeyValue> cbCard;

    @FXML
    private TextField tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldDepartment, tfOldMRP;

    @FXML
    private DatePicker dpDocumentDate, dpWriteoffDate;

    private Stage stage;

    private MyKeyValue oldDepartment, oldMRP;
    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfDocumentName, tfDocumentNumber);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfResidualCost);
    }

    public void btnSaveAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(cbCard);
        check|=EmptyChecker.isAnyEmpty(tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldDepartment, tfOldMRP);
        check|=EmptyChecker.isAnyEmpty(dpDocumentDate, dpWriteoffDate);

        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null,
                    "Основные поля (*) не должны быть пустыми.");
            return;
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите списать выбранный объект?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();

        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("insert into movements(card_number, document_name, " +
                    "document_data, document_number, operation_type, residual_cost, old_department, new_department, " +
                    "old_mrp, new_mrp) values(?,?,?,?,?,?,?,?,?,?)");
            Statement st=connection.createStatement();
            ResultSet rs=st.executeQuery("select id_operation_type_movements from operation_type_movements_directory" +
                    " where operation_type_name=\'Списание\'");
            if(!rs.next())
            {
                st.executeUpdate("insert into operation_type_movements_directory(operation_type_name) values ('Внутренее перемещение')");
                rs=st.executeQuery("select id_operation_type_movements from operation_type_movements_directory" +
                        " where operation_type_name=\'Списание\'");
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
            ps.setInt(i++,oldDepartment.getKey());
            ps.setInt(i++, oldMRP.getKey());
            ps.setInt(i++,oldMRP.getKey());


            ps.execute();
            ps.close();

            PreparedStatement ps1=connection.prepareStatement("update inventory_card set writeoff_date=? where id_inventory_card=?");

            ps1.setDate(1,Date.valueOf(dpWriteoffDate.getValue()));
            ps1.setInt(2, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps1.execute();
            ps1.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Списание успешно выполнено");
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

    public void btnCancelAction(ActionEvent actionEvent)
    {
        stage.close();
    }

    public void setStage(Stage stage)
    {
        this.stage =stage;

        this.stage.setOnShown(event ->
        {
            FieldCleaner.clear(tfDocumentName, tfDocumentNumber, tfResidualCost, tfOldDepartment, tfOldMRP);
            FieldCleaner.clear(cbCard);
            FieldCleaner.clear(dpDocumentDate, dpWriteoffDate);

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
        });
    }

    public void cbCardAction(ActionEvent actionEvent)
    {
        if(cbCard.getSelectionModel().isEmpty()) return;
        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();

        oldDepartment=Fixed_asset_accounting.getMyDatabase().getCardDepartment(cbCard.getSelectionModel().getSelectedItem().getKey());
        oldMRP=Fixed_asset_accounting.getMyDatabase().getCardMRP(cbCard.getSelectionModel().getSelectedItem().getKey());

        tfOldDepartment.setText(oldDepartment.getValue());
        tfOldMRP.setText(oldMRP.getValue());
    }
}
