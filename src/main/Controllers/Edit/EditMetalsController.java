package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.NobleMetals;
import main.others.Utils.*;

import java.sql.*;

public class EditMetalsController extends EditController<NobleMetals>
{
    @FXML
    private TextField tfUnit, tfMass, tfCount,tfNumber,tfMetalName;

    @FXML
    private ComboBox<MyKeyValue> cbCard;

    @FXML
    private Button btnSave;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfCount, tfMass);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfUnit, tfNumber, tfMetalName);
    }
    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfUnit, tfMass, tfCount,tfNumber,tfMetalName);
        check|=EmptyChecker.isAnyEmpty(cbCard);
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


    public void setParameters(int edit, NobleMetals object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfMetalName, tfCount, tfMass, tfNumber, tfUnit);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());

            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getCard()));
            cbCard.getSelectionModel().select(0);

            tfMetalName.setText(getCurrentObject().getName());
            tfNumber.setText(getCurrentObject().getNumber());
            tfUnit.setText(getCurrentObject().getUnit());
            tfCount.setText(String.valueOf(getCurrentObject().getCount()));
            tfMass.setText(String.valueOf(getCurrentObject().getMass()));

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
        tfMetalName.setEditable(editable);
        tfNumber.setEditable(editable);
        tfUnit.setEditable(editable);
        tfCount.setEditable(editable);
        tfMass.setEditable(editable);

        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject()
    {

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите добавить запись?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("insert into noble_metals values (?,?,?,?,?,?,?)");

            int i=1;
            ps.setNull(i++, Types.INTEGER);
            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfMetalName.getText());
            ps.setString(i++, tfNumber.getText());
            ps.setString(i++, tfUnit.getText());
            ps.setInt(i++, Integer.parseInt(tfCount.getText()));
            ps.setInt(i++, Integer.parseInt(tfMass.getText()));

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Запись успешно добавлена");
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
            PreparedStatement ps=connection.prepareStatement("update noble_metals set card_number=?, " +
                    "name_metal=?, number=?, unit=?, count=?, " +
                    "mass=? where id_noble_metals=?");
            int i=1;

            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfMetalName.getText());
            ps.setString(i++, tfNumber.getText());
            ps.setString(i++, tfUnit.getText());
            ps.setInt(i++, Integer.parseInt(tfCount.getText()));
            ps.setInt(i++, Integer.parseInt(tfMass.getText()));
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
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate("delete from noble_metals where id_noble_metals="+getCurrentObject().getId());

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

