package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Depreciation;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditDepreciationController extends EditController<Depreciation> {

    @FXML
    private TextField tfMonth1, tfMonth10, tfMonth11, tfMonth12,tfMonth7, tfMonth6, tfMonth9, tfMonth8,
            tfYear, tfMonth3, tfMonth2, tfMonth5, tfMonth4, tfCard;

    @FXML
    private Button btnSave;
    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfYear);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfMonth1, tfMonth2,
                tfMonth3, tfMonth4,tfMonth5, tfMonth6,tfMonth7, tfMonth8,tfMonth9, tfMonth10,tfMonth11, tfMonth12);
    }

    public void setParameters(int edit, Depreciation object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;
            tfCard.setText(String.valueOf(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(object.getCard()).get(0).toString()));
            tfYear.setText(String.valueOf(getCurrentObject().getYear()));
            tfMonth1.setText(String.valueOf(getCurrentObject().getM1()));
            tfMonth2.setText(String.valueOf(getCurrentObject().getM2()));
            tfMonth3.setText(String.valueOf(getCurrentObject().getM3()));
            tfMonth4.setText(String.valueOf(getCurrentObject().getM4()));
            tfMonth5.setText(String.valueOf(getCurrentObject().getM5()));
            tfMonth6.setText(String.valueOf(getCurrentObject().getM6()));
            tfMonth7.setText(String.valueOf(getCurrentObject().getM7()));
            tfMonth8.setText(String.valueOf(getCurrentObject().getM8()));
            tfMonth9.setText(String.valueOf(getCurrentObject().getM9()));
            tfMonth10.setText(String.valueOf(getCurrentObject().getM10()));
            tfMonth11.setText(String.valueOf(getCurrentObject().getM11()));
            tfMonth12.setText(String.valueOf(getCurrentObject().getM12()));

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

        tfMonth1.setEditable(editable);
        tfMonth2.setEditable(editable);
        tfMonth3.setEditable(editable);
        tfMonth4.setEditable(editable);
        tfMonth5.setEditable(editable);
        tfMonth6.setEditable(editable);
        tfMonth7.setEditable(editable);
        tfMonth8.setEditable(editable);
        tfMonth9.setEditable(editable);
        tfMonth10.setEditable(editable);
        tfMonth11.setEditable(editable);
        tfMonth12.setEditable(editable);
        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject() {

    }

    @Override
    protected void editObject()
    {

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите отредактировать запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            //проверка на редактирование и на год
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("update depreciation set " +
                    "janyary=?,february=?,march=?,april=?,may=?,june=?,july=?,august=?," +
                    "september=?,october=?,november=?,december=? where id_depreciation=?");
            int i=1;

            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth1.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth2.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth3.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth4.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth5.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth6.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth7.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth8.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth9.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth10.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth11.getText())));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfMonth12.getText())));
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операця завершена", null, "Запись успешно отредактирована");
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

            statement.executeUpdate("delete from depreciation where id_depreciation="+getCurrentObject().getId());
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

    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfMonth1, tfMonth10, tfMonth11, tfMonth12,tfMonth7, tfMonth6, tfMonth9, tfMonth8,
                tfYear, tfMonth3, tfMonth2, tfMonth5, tfMonth4, tfCard);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Основные поля (*) должны быть заполнены");
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

}
