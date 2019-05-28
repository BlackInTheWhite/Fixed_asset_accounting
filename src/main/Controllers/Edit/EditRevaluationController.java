package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Revaluation;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditRevaluationController extends EditController<Revaluation> {

    @FXML
    private TextField tfCoeff, tfAmount;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ComboBox<MyKeyValue> cbCard;

    @FXML
    private Button btnSave;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfAmount, tfCoeff);
        dpDate.setOnMouseClicked(event ->
        {
            if(!dpDate.isEditable())
                dpDate.hide();
        });
    }

    @FXML
    void btnSaveAction(ActionEvent event)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfCoeff, tfAmount);
        check|=EmptyChecker.isAnyEmpty(dpDate);
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

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> setResult(ResultTypes.CANCEL));
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
            PreparedStatement ps=connection.prepareStatement("insert into revaluation values (?,?,?,?,?)");
            int i=1;

            ps.setNull(i++, Types.INTEGER);

            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setDate(i++, Date.valueOf(dpDate.getValue()));
            ps.setDouble(i++, Double.parseDouble(tfCoeff.getText()));
            ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfAmount.getText())));

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
            PreparedStatement ps=connection.prepareStatement("update revaluation set card_number=?, " +
                    "date=?, coefficient=?, replacement_cost=? where id_revaluation=?");
            int i=1;

            ps.setInt(i++, cbCard.getSelectionModel().getSelectedItem().getKey());
            ps.setDate(i++, Date.valueOf(dpDate.getValue()));
            ps.setDouble(i++, Double.parseDouble(tfCoeff.getText()));
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
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить запись?"))
            return;

        Connection connection=Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate("delete from revaluation where id_revaluation="+getCurrentObject().getId());

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

    public void setParameters(int edit, Revaluation object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.ADDING)
        {
            editable=true;
            FieldCleaner.clear(tfCoeff, tfAmount);
            FieldCleaner.clear(cbCard);
            FieldCleaner.clear(dpDate);
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getNotWriteOffCardList());

            stage.setTitle("Добавление записи");
            btnSave.setText("Добавить");
        }
        else if(edit==EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;
            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getOneCardAsList(getCurrentObject().getCard()));
            cbCard.getSelectionModel().select(0);

            dpDate.setValue(getCurrentObject().getDate().toLocalDate());
            tfAmount.setText(String.valueOf(getCurrentObject().getCost()));
            tfCoeff.setText(String.valueOf(getCurrentObject().getCoefficient()));

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
        tfCoeff.setEditable(editable);
        tfAmount.setEditable(editable);
        dpDate.setEditable(editable);

        btnSave.setVisible(!(edit==EditTypes.VIEW));
    }

}
