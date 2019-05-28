package main.Controllers.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.InventoryCard;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

public class EditInventoryCardController extends EditController<InventoryCard> {

    @FXML
    private TextField tfUsefulLife, tfMRP, tfInventoryNumber, tfParticipants, tfStructElements, tfOKOF,
                    tfSerialNumber, tfInitialCost, tfDepartmentNumber, tfNotice, tfDepartment,
            tfDeviceCharacteristic, tfAccountNumber, tfOKUD, tfPassportNumber, tfOwnershipShare,
            tfLocation, tfMainObjectCharacteristic, tfOrganizationName, tfOKPO, tfCardNumber,
            tfObjectName, tfManufacturer;


    @FXML
    private ComboBox<MyKeyValue> cbDepreciationGroup, cbCRP;

    @FXML
    private DatePicker dpWriteoffDate, dpDeliveryDate, dpCardDate;

    @FXML
    private Button btnSave;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfCardNumber, tfUsefulLife,
                tfOwnershipShare);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfOrganizationName, tfObjectName,
                tfOKUD, tfOKPO, tfOKOF, tfPassportNumber, tfSerialNumber, tfInventoryNumber, tfAccountNumber,
                tfLocation, tfDepartmentNumber, tfManufacturer, tfParticipants,tfStructElements,tfDeviceCharacteristic,
                tfNotice, tfMainObjectCharacteristic);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfInitialCost);

        dpWriteoffDate.setOnMouseClicked(event ->
        {
            if (!dpWriteoffDate.isEditable())
                dpWriteoffDate.hide();
        });

        dpCardDate.setOnMouseClicked(event ->
        {
            if (!dpCardDate.isEditable())
                dpCardDate.hide();
        });

        dpDeliveryDate.setOnMouseClicked(event ->
        {
            if (!dpDeliveryDate.isEditable())
                dpDeliveryDate.hide();
        });
    }

    @FXML
    void btnSaveAction(ActionEvent event)
    {

        boolean check= EmptyChecker.isAnyEmpty(tfCardNumber, tfOrganizationName, tfObjectName,
                tfPassportNumber, tfSerialNumber, tfInventoryNumber, tfAccountNumber, tfLocation, tfManufacturer,
                tfInitialCost, tfUsefulLife);
        check|=EmptyChecker.isAnyEmpty(cbDepreciationGroup, cbCRP);
        check|=EmptyChecker.isAnyEmpty(dpCardDate, dpDeliveryDate);
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) должны быть заполнены.");
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

    public void setParameters(int edit, InventoryCard object)
    {
        super.setParameters(edit, object);
        boolean editable=false;
        if(edit== EditTypes.EDITING || edit==EditTypes.DELETING || edit==EditTypes.VIEW)
        {
            editable=edit==EditTypes.EDITING;

            tfCardNumber.setText(String.valueOf(getCurrentObject().getCardNumber()));
            tfOrganizationName.setText(getCurrentObject().getOrganization());
            tfDepartment.setText(Fixed_asset_accounting.getMyDatabase().getDepartmentByID(getCurrentObject().getDepartment()));
            tfObjectName.setText(getCurrentObject().getObjectName());
            tfOKUD.setText(getCurrentObject().getOKUD());
            tfOKPO.setText(getCurrentObject().getOKPO());
            tfOKOF.setText(getCurrentObject().getOKOF());
            tfPassportNumber.setText(getCurrentObject().getPassportNumber());
            tfSerialNumber.setText(getCurrentObject().getSerialNumber());
            tfInventoryNumber.setText(getCurrentObject().getInventoryNumber());
            tfStructElements.setText(getCurrentObject().getStructElemnts());
            tfMainObjectCharacteristic.setText(getCurrentObject().getMainObjectCharacteristics());
            tfAccountNumber.setText(getCurrentObject().getAccountnumber());
            tfLocation.setText(getCurrentObject().getLocation());
            tfDepartmentNumber.setText(getCurrentObject().getDepartmentNumber());
            tfManufacturer.setText(getCurrentObject().getManufacturer());
            tfInitialCost.setText(String.valueOf(getCurrentObject().getInitialCost()));
            tfUsefulLife.setText(String.valueOf(getCurrentObject().getUsefulLife()));
            tfMRP.setText(Fixed_asset_accounting.getMyDatabase().getMRPByID(getCurrentObject().getMrp()));
            tfParticipants.setText(getCurrentObject().getParticipants());
            tfOwnershipShare.setText(String.valueOf(getCurrentObject().getOwnershipShare()));
            tfDeviceCharacteristic.setText(getCurrentObject().getDeviceCharacteristics());
            tfNotice.setText(getCurrentObject().getNotice());

            dpDeliveryDate.setValue(getCurrentObject().getDeliveryDate().toLocalDate());
            dpCardDate.setValue(getCurrentObject().getCardDate().toLocalDate());
            if(getCurrentObject().getWriteoffDate()!=null)
                dpWriteoffDate.setValue(getCurrentObject().getWriteoffDate().toLocalDate());
            else
                dpWriteoffDate.setValue(null);

            if(edit==EditTypes.EDITING)
            {
                cbDepreciationGroup.setItems(Fixed_asset_accounting.getMyDatabase().getListFromDBTable(
                        "depreciation_group_directory", "id_depreciation_group_directory", "group_name"));
                cbCRP.setItems(Fixed_asset_accounting.getMyDatabase().getResponsiblePersonList("crp"));

                for(MyKeyValue kv:cbCRP.getItems())
                    if(kv.getKey()==getCurrentObject().getCrp())
                    {
                        cbCRP.getSelectionModel().select(kv);
                        break;
                    }

                for(MyKeyValue kv:cbDepreciationGroup.getItems())
                    if(kv.getKey()==getCurrentObject().getDerpreciationGroupNumber())
                    {
                        cbDepreciationGroup.getSelectionModel().select(kv);
                        break;
                    }

                btnSave.setText("Редактировать");
                stage.setTitle("Редактирование записи");
            }
            else
            {
                cbDepreciationGroup.setItems(Fixed_asset_accounting.getMyDatabase().getOneGroupAsList(object.getDerpreciationGroupNumber()));
                cbDepreciationGroup.getSelectionModel().select(0);

                cbCRP.setItems(Fixed_asset_accounting.getMyDatabase().getOneRPAsList("crp", "id_crp", object.getCrp()));
                cbCRP.getSelectionModel().select(0);
            }

            if(edit==EditTypes.DELETING)
            {
                btnSave.setText("Удалить");
                stage.setTitle("Удаление записи");
            }
            else if(edit==EditTypes.VIEW)
            {
                stage.setTitle("Просмотр записи");
            }

        }

        tfUsefulLife.setEditable(editable);
        tfInventoryNumber.setEditable(editable);
        tfParticipants.setEditable(editable);
        tfStructElements.setEditable(editable);
        tfOKOF.setEditable(editable);

        tfSerialNumber.setEditable(editable);
        tfInitialCost.setEditable(editable);
        tfDepartmentNumber.setEditable(editable);
        tfNotice.setEditable(editable);

        tfDeviceCharacteristic.setEditable(editable);
        tfAccountNumber.setEditable(editable);
        tfOKUD.setEditable(editable);
        tfPassportNumber.setEditable(editable);
        tfOwnershipShare.setEditable(editable);

        tfLocation.setEditable(editable);
        tfMainObjectCharacteristic.setEditable(editable);
        tfOrganizationName.setEditable(editable);
        tfOKPO.setEditable(editable);
        tfCardNumber.setEditable(editable);

        tfObjectName.setEditable(editable);
        tfManufacturer.setEditable(editable);
        dpCardDate.setEditable(editable);
        dpDeliveryDate.setEditable(editable);
        btnSave.setVisible(!(edit== EditTypes.VIEW));

    }

    @Override
    protected void addObject()
    {

    }

    @Override
    protected void editObject()
    {


        Connection connection = Fixed_asset_accounting.getMyDatabase().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from inventory_card where card_number=" +
                    tfCardNumber.getText());
            if (rs.next())
            {
                if(!(getCurrentObject().getCardNumber()==Integer.parseInt(tfCardNumber.getText()))) {
                    AlertWindows.showErrorAlert("Ошибка", null, "Такой номер карточки уже существует.\n");
                    rs.close();
                    return;
                }
            }

            rs = statement.executeQuery("select * from inventory_card where inventory_number=" +
                    tfInventoryNumber.getText());
            if (rs.next())
            {
                if(!getCurrentObject().getInventoryNumber().toLowerCase().equals(tfInventoryNumber.getText().toLowerCase())) {
                    AlertWindows.showErrorAlert("Ошибка", null, "Такой инвентарный уже существует.\n");
                    rs.close();
                    return;
                }
            }

            if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите отредактировать запись?"))
                return;

            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("update inventory_card set card_number=?, " +
                    "organization_name=?, " +
                    "department=?, card_date=?, object_name=?, OKYD=?, OKPO=?, OKOF=?, depreciation_group_number=?, " +
                    "passport_number=?, " +
                    "serial_number=?, inventory_number=?, delivery_date=?, writeoff_date=?, account_number=?, " +
                    "location=?, " +
                    "department_number=?, manufacturer=?, initial_cost=?, useful_life=?, person_responsible_for_card=?, " +
                    "MRP=?, " +
                    "participants=?, ownership_share=?, name_of_structural_elements_etc=?, " +
                    "characteristics_main_object=?, " +
                    "characteristics_of_the_devices_etc=?, notice=? where id_inventory_card=? ");

            ps.setInt(1, Integer.parseInt(tfCardNumber.getText()));
            ps.setString(2, tfOrganizationName.getText());
            ps.setInt(3, getCurrentObject().getDepartment());
            ps.setDate(4, Date.valueOf(dpCardDate.getValue()));
            ps.setString(5, tfObjectName.getText());
            ps.setString(6, tfOKUD.getText());
            ps.setString(7, tfOKPO.getText());
            ps.setString(8, tfOKOF.getText());
            ps.setInt(9, cbDepreciationGroup.getSelectionModel().getSelectedItem().getKey());
            ps.setString(10, tfPassportNumber.getText());
            ps.setString(11, tfSerialNumber.getText());
            ps.setString(12, tfInventoryNumber.getText());
            ps.setDate(13, Date.valueOf(dpDeliveryDate.getValue()));
            if(dpWriteoffDate.getValue()==null)
            {
                ps.setNull(14, Types.DATE);
            }
            else
                ps.setDate(14, Date.valueOf(dpWriteoffDate.getValue()));

            ps.setString(15, tfAccountNumber.getText());
            ps.setString(16, tfLocation.getText());
            ps.setString(17, tfDepartmentNumber.getText());
            ps.setString(18, tfManufacturer.getText());
            ps.setBigDecimal(19, BigDecimal.valueOf(Double.parseDouble(tfInitialCost.getText())));
            ps.setInt(20, Integer.parseInt(tfUsefulLife.getText()));
            ps.setInt(21, cbCRP.getSelectionModel().getSelectedItem().getKey());
            ps.setInt(22, getCurrentObject().getMrp());
            ps.setString(23, tfParticipants.getText());
            ps.setInt(24, Integer.parseInt(tfOwnershipShare.getText()));
            ps.setString(25, tfStructElements.getText());
            ps.setString(26, tfMainObjectCharacteristic.getText());
            ps.setString(27, tfDeviceCharacteristic.getText());
            ps.setString(28, tfNotice.getText());

            ps.setInt(29, getCurrentObject().getId());

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
        finally
        {
            try
            {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
    }

    @Override
    protected void delObject()
    {
        if(!AlertWindows.showAcceptAlert("Подтверждение",null,"Вы действительно хотите удалить инвентарную карточку?"))
            return;
        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();

            int i=1;
            statement.executeUpdate("delete from changes_in_cost where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from depreciation where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from devices where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from movements where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from noble_metals where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from repair_costs where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from revaluation where card_number="+getCurrentObject().getId());
            statement.executeUpdate("delete from transfers where id_transfers="+getCurrentObject().getId());
            statement.executeUpdate("delete from transmission_info where id_transmission_info="+getCurrentObject().getId());
            statement.executeUpdate("delete from inventory_card where id_inventory_card="+getCurrentObject().getId());

            statement.close();

            connection.commit();
            AlertWindows.showInfoAlert("Операция завершена",null,"Инвентарная карточка успешно удалена");
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
