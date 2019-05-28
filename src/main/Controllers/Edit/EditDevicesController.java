package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Devices;
import main.others.Utils.*;

import java.sql.*;

public class EditDevicesController extends EditController<Devices> {

    @FXML
    private TextField tfCount, tfName;

    @FXML
    private ComboBox<MyKeyValue> cbCard;

    @FXML
    private Button btnSave;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyLettersPattern, tfName);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfCount);
    }
    public void setStage(Stage stage)
    {
        this.stage=stage;
        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
    }

    public void btnSaveAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfCount, tfName);
        if(check)
        {
            AlertWindows.showErrorAlert("Error", null, "Основные поля (*) должны быть заполнены");
            return;
        }

        doOperation();
    }

    public void btnCloseAction(ActionEvent actionEvent)
    {
        setResult(ResultTypes.CANCEL);
        stage.close();
    }


    public void setParameters(int edit, Devices object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfName, tfCount);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());

            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getCard()));
            cbCard.getSelectionModel().select(0);

            tfName.setText(getCurrentObject().getName());
            tfCount.setText(String.valueOf(getCurrentObject().getCount()));
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
        tfCount.setEditable(editable);
        tfName.setEditable(editable);
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
            PreparedStatement ps=connection.prepareStatement("insert into devices values (?,?,?,?)");
            int i=1;

            ps.setNull(i++, Types.INTEGER);
            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfName.getText());
            ps.setInt(i++, Integer.parseInt(tfCount.getText()));

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

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement("update devices set card_number=?, " +
                    "device_name=?, count=? where id_devices=?");
            int i=1;


            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setString(i++, tfName.getText());
            ps.setInt(i++, Integer.parseInt(tfCount.getText()));
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операя завершена",null,"Запись успешно отредактирована");

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

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();

            statement.executeUpdate("delete from devices where id_devices="+getCurrentObject().getId());
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
