package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Fixed_asset_accounting;
import main.others.Utils.*;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Date;

public class ReportsController {

    @FXML
    private TextField tfYearStart, tfYearEnd;

    @FXML
    private DatePicker dpDateStart, dpDateEnd;

    @FXML
    private ComboBox<MyReportTableString> cbReportType;

    @FXML
    private ComboBox<MyKeyValue> cbCard;

    private Stage stage;

    @FXML
    public void initialize()
    {
        FieldPattern.setPatternToFieldProperty(FieldPattern.year, tfYearStart, tfYearEnd);
        ObservableList<MyReportTableString> list= FXCollections.observableArrayList();
        list.add(new MyReportTableString(MyReport.OBJECTS_BY_DELIVERY_DATE, "Объекты по дате принятия к учету"));
        list.add(new MyReportTableString(MyReport.OBJECTS_BY_WRITEOFF_DATE, "Объекты по дате списания с учета"));
        list.add(new MyReportTableString(MyReport.COST_CHANGES_BY_PERIOD, "Изменения стоимости за период"));
        list.add(new MyReportTableString(MyReport.MOVEMENTS_BY_PERIOD, "Внутренние перемещения за период"));
        list.add(new MyReportTableString(MyReport.REPAIR_BY_PERIOD, "Ремонты за период"));

        cbReportType.setItems(list);
    }

    @FXML
    void btnCardReportAction(ActionEvent event)
    {
        if(cbCard.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Карточка не выбрана", null, "Выберите карточку для формирования отчета");
            return;
        }
        JasperPrint jasperPrint=MyReport.getCardReport(cbCard.getSelectionModel().getSelectedItem().getKey());
        if(jasperPrint!=null) JasperViewer.viewReport(jasperPrint, false);
    }

    @FXML
    void btnDepreciationReportAction(ActionEvent event)
    {
        if(EmptyChecker.isAnyEmpty(tfYearStart, tfYearEnd))
        {
            AlertWindows.showErrorAlert("Ошибка ввода", null, "Введите данные для формирования отчета");
            return;
        }
        JasperPrint jasperPrint=MyReport.getDepreciationReport(Integer.parseInt(tfYearStart.getText()), Integer.parseInt(tfYearEnd.getText()));
        if(jasperPrint!=null)
            JasperViewer.viewReport(jasperPrint, false);

    }

    @FXML
    void btnDateReportAction(ActionEvent event)
    {

        if(cbReportType.getSelectionModel().isEmpty())
        {
            AlertWindows.showErrorAlert("Отчет не выбран", null, "Выберите отчёт для формирования");
            return;
        }

        if(EmptyChecker.isAnyEmpty(dpDateEnd, dpDateStart))
        {
            AlertWindows.showErrorAlert("Ошибка ввода", null, "Введите данные для формирования отчета");
            return;
        }
        JasperPrint jasperPrint=MyReport.getReportByDatePeriod(cbReportType.getSelectionModel().getSelectedItem().getReportName(),
                Date.valueOf(dpDateStart.getValue()), Date.valueOf(dpDateEnd.getValue()));
        if(jasperPrint!=null) JasperViewer.viewReport(jasperPrint, false);
    }

    @FXML
    void btnCloseAction(ActionEvent event)
    {
        stage.close();
    }

    public void setStage(Stage stage)
    {
        this.stage=stage;

        this.stage.setOnShown(event ->
        {
            FieldCleaner.clear(tfYearEnd, tfYearStart);
            FieldCleaner.clear(dpDateEnd, dpDateStart);
            FieldCleaner.clear(cbCard, cbReportType);

            cbCard.setItems(Fixed_asset_accounting.getMyDatabase().getCardListFromDBTable());
        });
    }

}
