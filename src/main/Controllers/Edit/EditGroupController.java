package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.DepreciationGroup;
import main.others.Utils.*;

import java.sql.*;

/**
 * Created by User on 07.06.2018.
 */
public class EditGroupController extends EditController<DepreciationGroup>
{
    @FXML
    public TextField tfDepreciationName, tfDepreciationNumber, tfDepreciationCharacteristc;
    private Stage stage;

    @FXML
    private Button btnSave;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfDepreciationCharacteristc, tfDepreciationName, tfDepreciationNumber);
    }

    public void btnSaveGroupAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfDepreciationCharacteristc, tfDepreciationName, tfDepreciationNumber);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) не должны быть пустыми.");
            return;
        }

        doOperation();
    }

    public void btnCloseAction(ActionEvent actionEvent)
    {
        setResult(ResultTypes.CANCEL);
        stage.close();
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
    }

    public void setParameters(int edit, DepreciationGroup object)
    {
        super.setParameters(edit, object);

        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfDepreciationCharacteristc, tfDepreciationName, tfDepreciationNumber);
            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");

        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=(edit==EditTypes.EDITING);
            tfDepreciationCharacteristc.setText(getCurrentObject().getCharacteristic());
            tfDepreciationName.setText(getCurrentObject().getName());
            tfDepreciationNumber.setText(getCurrentObject().getNumber());
            if(edit==EditTypes.EDITING)
            {
                stage.setTitle("Редактирование записи");
                btnSave.setText("Редактировать");
            }
            else if(edit==EditTypes.VIEW)
            {
                stage.setTitle("Просмотр записи");
            }
            else
            {
                stage.setTitle("Удаление записи");
                btnSave.setText("Удалить");
            }

            tfDepreciationName.setEditable(editable);
            tfDepreciationCharacteristc.setEditable(editable);
            tfDepreciationNumber.setEditable(editable);

            btnSave.setVisible(!(edit==EditTypes.VIEW));

        }
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
            PreparedStatement  ps = connection.prepareStatement("insert into depreciation_group_directory values(?,?,?,?)");
            int i=1;

            ps.setNull(i++, Types.INTEGER);
            ps.setString(i++, tfDepreciationNumber.getText());
            ps.setString(i++, tfDepreciationName.getText());
            ps.setString(i++, tfDepreciationCharacteristc.getText());

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
            PreparedStatement                 ps=connection.prepareStatement("update depreciation_group_directory " +
                    "set group_number=?, group_name=?, characteristic=? where id_depreciation_group_directory=?");

            int i=1;

            ps.setString(i++, tfDepreciationNumber.getText());
            ps.setString(i++, tfDepreciationName.getText());
            ps.setString(i++, tfDepreciationCharacteristc.getText());
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
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить выбранную запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate("delete from depreciation_group_directory where id_depreciation_group_directory="+getCurrentObject().getId());

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
