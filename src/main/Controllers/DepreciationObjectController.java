package main.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by User on 04.06.2018.
 */
public class DepreciationObjectController
{
    @FXML
    private ComboBox<MyKeyValue> cbCard;
    @FXML
    private TextField tfYear, tfValue;
    @FXML
    private ComboBox<String> cbMonth;

    private Stage stage;
    private final String[] months={"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    private final String[] monthsDB={"janyary","february","march","april","may","june","july","august","september","october","november","december"};
    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.year, tfYear);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfValue);
    }

    public void cbCardAction(ActionEvent actionEvent)
    {
        if(cbCard.getSelectionModel().isEmpty()) return;
        FieldCleaner.clear(tfValue, tfYear);
        FieldCleaner.clear(cbMonth);
    }

    public void btnSaveAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(cbCard, cbMonth);
        check|=EmptyChecker.isAnyEmpty(tfYear, tfValue);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля(*) должны быть заполнены");
            return;
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите начислить амортизацию?"))
            return;
        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from depreciation where card_number="+cbCard.getSelectionModel().getSelectedItem().getKey()
                    +" and depreciation.year="+Integer.parseInt(tfYear.getText()));
            if(!rs.next())
            {
                statement.execute("insert into depreciation(card_number, year) values("+cbCard.getSelectionModel().getSelectedItem().getKey()+", "+Integer.parseInt(tfYear.getText())+")");
            }

            PreparedStatement ps=connection.prepareStatement("update depreciation set "+
                    monthsDB[cbMonth.getSelectionModel().getSelectedIndex()]+"=? where card_number=? and depreciation.year=?");
            ps.setBigDecimal(1, BigDecimal.valueOf(Double.parseDouble(tfValue.getText())));
            ps.setInt(2, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setInt(3, Integer.parseInt(tfYear.getText()));
            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена", null, "Амортизация успешно начислена");
            cbCardAction(null);
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

    public void btnCancelAction(ActionEvent actionEvent) {
        stage.close();
    }


    public void setStage(Stage stage)
    {
        this.stage =stage;

        this.stage.setOnShown(event -> {

            FieldCleaner.clear(cbCard, cbMonth);
            FieldCleaner.clear(tfYear, tfValue);

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());
            cbMonth.setItems(FXCollections.observableArrayList(months));
        });
    }
}
