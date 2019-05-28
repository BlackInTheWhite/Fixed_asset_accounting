package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.ResponsiblePerson;
import main.others.Utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by User on 07.06.2018.
 */
public class EditRPController extends EditController<ResponsiblePerson>
{

    private static final String INSERT="insert into %s values(?,?,?,?,?,?)";
    private static final String UPDATE="update %s set surname=?, name=?, patronymic=?, position=?, personnel_number=? where %s=?";
    private Stage stage;

    @FXML
    private TextField tfSurname, tfName, tfPatronymic, tfNumber, tfPosition;

    @FXML
    private Button btnSave;
    private String table, column;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyLettersPattern, tfName, tfSurname, tfPatronymic);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfPosition, tfNumber);
    }

    public void btnCloseAction(ActionEvent actionEvent)
    {
        setResult(ResultTypes.CANCEL);
        stage.close();
    }

    public void btnSaveRPAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfSurname,  tfName, tfPatronymic, tfNumber, tfPosition);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) не должны быть пустыми.");
            return;
        }

        doOperation();
    }

    public void setStage(Stage stage)
    {
        this.stage =stage;

        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
    }

    public void setParameters(int edit, ResponsiblePerson object, String table, String column)
    {
        super.setParameters(edit, object);

        this.table=table;
        this.column=column;

        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfSurname, tfName, tfPatronymic, tfNumber, tfPosition);
            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.VIEW || edit==EditTypes.DELETING)
        {
            editable=(edit==EditTypes.EDITING);
            tfSurname.setText(getCurrentObject().getSurname());
            tfName.setText(getCurrentObject().getName());
            tfPatronymic.setText(getCurrentObject().getPatronymic());
            tfNumber.setText(getCurrentObject().getNumber());
            tfPosition.setText(getCurrentObject().getPosition());
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

        tfSurname.setEditable(editable);
        tfName.setEditable(editable);
        tfPatronymic.setEditable(editable);
        tfPosition.setEditable(editable);
        tfNumber.setEditable(editable);
        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

    @Override
    protected void addObject()
    {
        if(Fixed_asset_accounting.getMyDatabase().checkForResponsiblePersonNumber(tfNumber.getText()))
        {
            AlertWindows.showErrorAlert("Ошибка", null, "Работник с таким номером уже существует.");
            return;
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите добавить нового работника?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(String.format(INSERT, table));
            int i=1;

            ps.setNull(i++, Types.INTEGER);
            ps.setString(i++, tfSurname.getText());
            ps.setString(i++, tfName.getText());
            ps.setString(i++, tfPatronymic.getText());
            ps.setString(i++, tfPosition.getText());
            ps.setString(i++, tfNumber.getText());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Работник успешно добавлен");
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
        if(Fixed_asset_accounting.getMyDatabase().checkForResponsiblePersonNumber(tfNumber.getText()))
        {
            if(!getCurrentObject().getNumber().toLowerCase().equals(tfNumber.getText().toLowerCase()))
            {
                AlertWindows.showErrorAlert("Ошибка", null, "Работник с таким номером уже существует.");
                return;
            }
        }

        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите изменить информацию работника?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement   ps=connection.prepareStatement(String.format(UPDATE, table, column));
            int i=1;

            ps.setString(i++, tfSurname.getText());
            ps.setString(i++, tfName.getText());
            ps.setString(i++, tfPatronymic.getText());
            ps.setString(i++, tfPosition.getText());
            ps.setString(i++, tfNumber.getText());
            ps.setInt(i++, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Редактирование успешно выполнено");
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
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить работника?"))
            return;

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);

            PreparedStatement ps=connection.prepareStatement("delete from "+table+" where "+column+"=?");

            ps.setInt(1, getCurrentObject().getId());

            ps.execute();
            ps.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена", null, "Работник успешно удалён");
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
