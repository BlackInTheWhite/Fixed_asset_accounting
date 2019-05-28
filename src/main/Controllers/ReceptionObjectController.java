package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Tables.Devices;
import main.others.Tables.NobleMetals;
import main.others.Tables.ResponsiblePerson;
import main.others.Utils.*;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by User on 28.05.2018.
 */
public class ReceptionObjectController
{
    @FXML
    public Tab tab3;
    @FXML
    private TextField tfCardNumber, tfOrganizationName, tfOKUD, tfOKPO, tfOKOF, tfObjectName, tfPassportNumber,
            tfSerialNumber, tfInventoryNumber, tfAccountNumber,  tfLocation, tfDepartmentNumber, tfManufacturer,
            tfInitialCost, tfUsefulLife, tfParticipants, tfOwnershipShare, tfStructuralElements,
            tfMainObjectsCharacteristics, tfDevicesCharacteristics, tfNotice, tfDocumentName,
            tfDocumentNumber, tfOperationPeriod, tfDepreciationAmount, tfResidualCost, tfDeviceName, tfDeviceCount,
            tfMetalName, tfMetalNumber, tfMetalUnit, tfMetalCount, tfMetalMass;

    @FXML
    private ComboBox<MyKeyValue> cbDepartment, cbDepreciationGroup;

    @FXML
    private ComboBox<ResponsiblePerson> cbCRP, cbMRP;

    @FXML
    private DatePicker dpCardDate, dpDeliveryDate, dpProductionDate, dpRepairDate, dpDocumentDate;

    @FXML
    private TableView<Devices> twDevices;

    @FXML
    private TableView<NobleMetals> twNobleMetals;

    private Stage stage;
    private final ObservableList<Devices> devices=FXCollections.observableArrayList();
    private final ObservableList<NobleMetals> metals=FXCollections.observableArrayList();
    private boolean isNew;

    public void setStage(Stage stage)
    {
        this.stage=stage;
        this.stage.setOnShown(event ->
        {
            FieldCleaner.clear(tfCardNumber, tfOrganizationName, tfOKUD, tfOKPO, tfOKOF, tfObjectName, tfPassportNumber,
                    tfSerialNumber, tfInventoryNumber, tfAccountNumber,  tfLocation, tfDepartmentNumber, tfManufacturer,
                    tfInitialCost, tfUsefulLife, tfParticipants, tfOwnershipShare, tfStructuralElements,
                    tfMainObjectsCharacteristics, tfDevicesCharacteristics, tfNotice, tfDocumentName,
                    tfDocumentNumber, tfOperationPeriod, tfDepreciationAmount, tfResidualCost, tfDeviceName,
                    tfDeviceCount, tfMetalName, tfMetalNumber, tfMetalUnit, tfMetalCount, tfMetalMass);

            FieldCleaner.clear(cbDepartment, cbCRP, cbMRP, cbDepreciationGroup);
            FieldCleaner.clear(dpCardDate, dpDeliveryDate, dpProductionDate, dpRepairDate, dpDocumentDate);
            devices.clear();
            metals.clear();

            cbDepartment.setItems(Fixed_asset_accounting.getMyDatabase().getListFromDBTable(
                    "department_directory", "id_department_directory", "department_name"));

            cbDepreciationGroup.setItems(Fixed_asset_accounting.getMyDatabase().getListFromDBTable(
                    "depreciation_group_directory", "id_depreciation_group_directory", "group_name"));

            cbCRP.setItems(Fixed_asset_accounting.getMyDatabase().getRPFromDBTable("crp"));
            cbMRP.setItems(Fixed_asset_accounting.getMyDatabase().getRPFromDBTable("mrp"));
        });
    }

    @FXML
    public void initialize()
    {

        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyNumbersPattern, tfCardNumber, tfUsefulLife,
                tfOwnershipShare, tfDeviceCount, tfMetalCount, tfMetalMass);
        FieldPattern.setPatternToFieldProperty(FieldPattern.numbersLettersPattern, tfOrganizationName, tfObjectName,
                tfOKUD, tfOKPO, tfOKOF, tfPassportNumber, tfSerialNumber, tfInventoryNumber, tfAccountNumber,
                tfLocation, tfDepartmentNumber, tfManufacturer, tfParticipants, tfStructuralElements,
                tfMainObjectsCharacteristics, tfDevicesCharacteristics, tfNotice, tfDeviceName, tfMetalName,
                tfMetalNumber, tfMetalUnit);
        FieldPattern.setPatternToFieldProperty(FieldPattern.onlyDecimalPattern, tfInitialCost);

        int i=0;
        String fieldNames[]={"name", "count"};

        for(TableColumn<Devices, ?> tc:twDevices.getColumns())
            tc.setCellValueFactory(new PropertyValueFactory<>(fieldNames[i++]));
        twDevices.setItems(devices);

        i=0;
        fieldNames=new String[]{"name", "number", "unit", "count", "mass"};
        for(TableColumn<NobleMetals, ?> tc:twNobleMetals.getColumns())
            tc.setCellValueFactory(new PropertyValueFactory<>(fieldNames[i++]));
        twNobleMetals.setItems(metals);
    }

    public void btnSaveAction(ActionEvent actionEvent)
    {
        boolean check= EmptyChecker.isAnyEmpty(tfCardNumber, tfOrganizationName, tfObjectName,
                tfPassportNumber, tfSerialNumber, tfInventoryNumber, tfAccountNumber, tfLocation, tfManufacturer,
                tfInitialCost, tfUsefulLife);
        check|=EmptyChecker.isAnyEmpty(cbDepartment, cbDepreciationGroup, cbCRP, cbMRP);
        check|=EmptyChecker.isAnyEmpty(dpCardDate, dpDeliveryDate);
        if(!isNew)
        {
            check|=EmptyChecker.isAnyEmpty(tfDocumentName, tfDocumentNumber, tfOperationPeriod, tfDepreciationAmount,
                    tfResidualCost);
            check|=EmptyChecker.isAnyEmpty(dpProductionDate, dpDocumentDate);
        }
        if(check)
        {
            AlertWindows.showErrorAlert("Ошибка ввода данных", null, "Основные поля (*) должны быть заполнены.");
            return;
        }

        Connection connection = Fixed_asset_accounting.getMyDatabase().getConnection();
            try
            {
                connection.setAutoCommit(false);

                Statement statement = connection.createStatement();
                PreparedStatement ps=connection.prepareStatement("select * from inventory_card where " +
                        "card_number=? or inventory_number=?");
                ps.setInt(1, Integer.parseInt(tfCardNumber.getText()));
                ps.setString(2, tfInventoryNumber.getText());

                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    AlertWindows.showErrorAlert("Ошибка ввода данных", null,
                            "Такой \"Номер карточки\" или \"Инвентарный номер\" уже существует");
                    return;
                }

                if(!AlertWindows.showAcceptAlert("Подтверждение", null, "Вы действительно хотите создать новую инвентарную карточку?"))
                    return;

                ps.close();
                ps = connection.prepareStatement("insert into inventory_card(card_number, organization_name, " +
                        "department, card_date, object_name, OKYD, OKPO, OKOF, depreciation_group_number, passport_number, " +
                        "serial_number, inventory_number, delivery_date, account_number, location, " +
                        "department_number, manufacturer, initial_cost, useful_life, person_responsible_for_card, MRP, " +
                        "participants, ownership_share, name_of_structural_elements_etc, characteristics_main_object, " +
                        "characteristics_of_the_devices_etc, notice) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                int i=1;
                ps.setInt(i++, Integer.parseInt(tfCardNumber.getText()));
                ps.setString(i++, tfOrganizationName.getText());
                ps.setInt(i++, cbDepartment.getSelectionModel().getSelectedItem().getKey());
                ps.setDate(i++, Date.valueOf(dpCardDate.getValue()));
                ps.setString(i++, tfObjectName.getText());
                ps.setString(i++, tfOKUD.getText());
                ps.setString(i++, tfOKPO.getText());
                ps.setString(i++, tfOKOF.getText());
                ps.setInt(i++, cbDepreciationGroup.getSelectionModel().getSelectedItem().getKey());
                ps.setString(i++, tfPassportNumber.getText());
                ps.setString(i++, tfSerialNumber.getText());
                ps.setString(i++, tfInventoryNumber.getText());
                ps.setDate(i++, Date.valueOf(dpDeliveryDate.getValue()));
                ps.setString(i++, tfAccountNumber.getText());
                ps.setString(i++, tfLocation.getText());
                ps.setString(i++, tfDepartmentNumber.getText());
                ps.setString(i++, tfManufacturer.getText());
                ps.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfInitialCost.getText())));
                ps.setInt(i++, Integer.parseInt(tfUsefulLife.getText()));
                ps.setInt(i++, cbCRP.getSelectionModel().getSelectedItem().getId());
                ps.setInt(i++, cbMRP.getSelectionModel().getSelectedItem().getId());
                ps.setString(i++, tfParticipants.getText());
                ps.setInt(i++, Integer.parseInt(tfOwnershipShare.getText()));
                ps.setString(i++, tfStructuralElements.getText());
                ps.setString(i++, tfMainObjectsCharacteristics.getText());
                ps.setString(i++, tfDevicesCharacteristics.getText());
                ps.setString(i++, tfNotice.getText());

                ps.execute();
                ps.close();

                rs = statement.executeQuery("select * from inventory_card where card_number=" +
                        tfCardNumber.getText());
                rs.next();

                if (devices.size() > 0)
                {
                    PreparedStatement ps1 = connection.prepareStatement("insert into devices(card_number, device_name, " +
                            "count) values(?,?,?)");
                    ps1.setInt(1, rs.getInt("id_inventory_card"));
                    for (Devices d : devices)
                    {
                        ps1.setString(2, d.getName());
                        ps1.setInt(3, d.getCount());
                        ps1.addBatch();
                    }
                    ps1.executeBatch();
                    ps1.close();
                }

                if (metals.size() > 0)
                {
                    PreparedStatement ps1 = connection.prepareStatement("insert into noble_metals(card_number, " +
                            "name_metal, number, unit, count, mass) values (?,?,?,?,?,?)");
                    ps1.setInt(1, rs.getInt("id_inventory_card"));
                    for (NobleMetals nb : metals)
                    {
                        ps1.setString(2, nb.getName());
                        ps1.setString(3, nb.getNumber());
                        ps1.setString(4, nb.getUnit());
                        ps1.setInt(5, nb.getCount());
                        ps1.setInt(6, nb.getMass());
                        ps1.addBatch();
                    }
                    ps1.executeBatch();
                    ps1.close();
                }

                if (!isNew)
                {
                    PreparedStatement ps1 = connection.prepareStatement("insert into transmission_info(id_transmission_info," +
                            "production_date, repair_date, document_name, document_number, document_date, " +
                            "operation_period, depreciation_amount, residual_cost) values(?,?,?,?,?,?,?,?,?)");
                    i=1;
                    //System.out.println(rs==null);
                    ps1.setInt(i++, rs.getInt("id_inventory_card"));
                    ps1.setDate(i++, Date.valueOf(dpProductionDate.getValue()));

                    if(dpRepairDate.getValue()==null)
                        ps1.setNull(i++, Types.DATE);
                    else
                        ps1.setDate(i++, Date.valueOf(dpRepairDate.getValue()));

                    ps1.setString(i++, tfDocumentName.getText());
                    ps1.setString(i++, tfDocumentNumber.getText());
                    ps1.setDate(i++, Date.valueOf(dpDocumentDate.getValue()));
                    ps1.setInt(i++, Integer.parseInt(tfOperationPeriod.getText()));
                    ps1.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfDepreciationAmount.getText())));
                    ps1.setBigDecimal(i++, BigDecimal.valueOf(Double.parseDouble(tfResidualCost.getText())));
                    ps1.execute();
                    ps1.close();
                }

                connection.commit();
                AlertWindows.showInfoAlert(null, null, "Инвентарная карточка была успешно создана");
                stage.close();

            }
            catch (SQLException e)
            {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
                try{
                    connection.rollback();
                } catch (SQLException e1) {
                    AlertWindows.showErrorAlert("SQLException", null, e1.getMessage());
                }

            }
            finally
            {
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

    public void btnDeviceAddAction(ActionEvent actionEvent)
    {
        if(EmptyChecker.isAnyEmpty(tfDeviceName, tfDeviceCount))
        {
            AlertWindows.showErrorAlert("Ошибка добавления устройства", null, "Все поля должны быть заполнены");
            return;
        }
        devices.add(new Devices(-1, -1,tfDeviceName.getText(), Integer.parseInt(tfDeviceCount.getText())));
        FieldCleaner.clear(tfDeviceName, tfDeviceCount);
    }

    public void btnDeviceDelAction(ActionEvent actionEvent)
    {
        if(twDevices.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Устройство не выбрано", null, "Выберите устройство для удаления");
            return;
        }
        twDevices.getItems().remove(twDevices.getSelectionModel().getSelectedItem());
    }

    public void btnMetalDelAction(ActionEvent actionEvent) {

        if(twNobleMetals.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Металл не выбран", null, "Выберите металл для удаления");
            return;
        }
        twNobleMetals.getItems().remove(twNobleMetals.getSelectionModel().getSelectedItem());
    }

    public void btnMetalAddAction(ActionEvent actionEvent)
    {
        if(EmptyChecker.isAnyEmpty(tfMetalName, tfMetalNumber, tfMetalUnit, tfMetalCount, tfMetalMass))
        {
            AlertWindows.showErrorAlert("Ошибка добавления металла", null, "Все поля должны быть заполнены");
            return;
        }
        metals.add(new NobleMetals(-1,-1,tfMetalName.getText(), tfMetalNumber.getText(), tfMetalUnit.getText(),
                Integer.parseInt(tfMetalCount.getText()), Integer.parseInt(tfMetalMass.getText())));
        FieldCleaner.clear(tfMetalName, tfMetalNumber, tfMetalUnit, tfMetalCount, tfMetalMass);
    }

    public void receptionObjectIsNew(boolean isNew)
    {
        tab3.setDisable(isNew);
        this.isNew=isNew;
    }
}
