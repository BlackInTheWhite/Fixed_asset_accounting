package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by User on 07.06.2018.
 */
public class EditValueController extends EditController<MyType>
{
    private static final String INSERT="insert into %s values(?,?)";
    private static final String UPDATE="update %s set %s=? where %s=?";

    @FXML
    private Label labelValue;

    @FXML
    private TextField tfValue;

    @FXML
    private Button btnSave;

    private Stage stage;
    private String table, idcolumn, insertColumn;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfValue);
    }
    public void btnSaveValueAction(ActionEvent actionEvent)
    {

        boolean check= EmptyChecker.isAnyEmpty(tfValue);
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


    public void setParameters(int edit, MyType object, String table, String idcolumn, String insertColumn)
    {
        super.setParameters(edit, object);
        this.table=table;
        this.idcolumn=idcolumn;
        this.insertColumn=insertColumn;

        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfValue);
            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.VIEW || edit==EditTypes.DELETING)
        {
            editable=(edit==EditTypes.EDITING);
            tfValue.setText(String.valueOf(getCurrentObject().getType()));

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
        }
        tfValue.setEditable(editable);
        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject()
    {
        if(Fixed_asset_accounting.getMyDatabase().checkForType(table, insertColumn, tfValue.getText()))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Такая запись уже существует.");
            return;
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите добавить запись?"))
            return;


        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(String.format(INSERT, table));
            int i=1;

            ps.setNull(i++, Types.INTEGER);
            ps.setString(i++, tfValue.getText());

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
        if(Fixed_asset_accounting.getMyDatabase().checkForType(table, insertColumn, tfValue.getText()))
        {
            if(!getCurrentObject().getType().toLowerCase().equals(tfValue.getText().toLowerCase()))
            {
                AlertWindows.showErrorAlert("Ошибка", null, "Такая запись уже существует.");
                return;
            }
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите отредактировать выбранную запись?"))
            return;


        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps=connection.prepareStatement(String.format(UPDATE, table, insertColumn, idcolumn));
            int i=1;

            ps.setString(i++, tfValue.getText());
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
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());try {
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


        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);

            PreparedStatement ps=connection.prepareStatement("delete from "+table+" where "+idcolumn+"=?");

            ps.setInt(1, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена", null, "Запись успешно удалена");
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
