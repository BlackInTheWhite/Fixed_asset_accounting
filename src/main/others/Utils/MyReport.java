package main.others.Utils;

import main.Controllers.ReportsController;
import main.Fixed_asset_accounting;
import main.others.Utils.AlertWindows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 16.06.2018.
 */
public class MyReport extends ReportsController {
    public static final String OBJECTS_BY_DELIVERY_DATE="/main/reports/InventoryCardByDeliveryDateReport.jasper";
    public static final String OBJECTS_BY_WRITEOFF_DATE="/main/reports/InventoryCardByWriteOffDateReport.jasper";
    public static final String COST_CHANGES_BY_PERIOD="/main/reports/CostChangesByDocumentDateReport.jasper";
    public static final String MOVEMENTS_BY_PERIOD="/main/reports/MovementsReportByDate.jasper";
    public static final String REPAIR_BY_PERIOD="/main/reports/RepairCostsByDocumentDateReport.jasper";
    public static final String DEPRECIATION_BY_PERIOD="/main/reports/DepreciationReportByYear.jasper";
    public static final String INVENTORY_CARD_BY_ID="/main/reports/InventoryCardReport.jasper";

    private static final String SUBREPORT_DIR="main/reports/";

    public static JasperPrint getReportByDatePeriod(String report, Date dateStart, Date dateEnd)
    {
        InputStream inputStream = Fixed_asset_accounting.class.getResourceAsStream(report);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DATE_START", dateStart);
        parameters.put("DATE_END", dateEnd);

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();

        JasperReport jasperReport=null;
        JasperPrint jasperPrint= null;
        try {
            jasperReport= (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        } catch (JRException e) {
            AlertWindows.showErrorAlert("JRException", null, e.getMessage());
            return null;
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                AlertWindows.showErrorAlert("IOException", null, e.getMessage());
            }
        }

        return jasperPrint;
    }

    public static JasperPrint getDepreciationReport(int yearStart, int yearEnd)
    {
        InputStream inputStream = Fixed_asset_accounting.class.getResourceAsStream(DEPRECIATION_BY_PERIOD);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("YEAR_START", yearStart);
        parameters.put("YEAR_END", yearEnd);

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();

        JasperReport jasperReport=null;
        JasperPrint jasperPrint= null;
        try {
            jasperReport= (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        } catch (JRException e) {
            AlertWindows.showErrorAlert("JRException", null, e.getMessage());
            return null;
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                AlertWindows.showErrorAlert("IOException", null, e.getMessage());
            }
        }

        return jasperPrint;
    }

    public static JasperPrint getCardReport(int cardID)
    {

        InputStream inputStream = Fixed_asset_accounting.class.getResourceAsStream(INVENTORY_CARD_BY_ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);
        parameters.put("CARD_ID", cardID);

        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();

        JasperReport jasperReport=null;
        JasperPrint jasperPrint= null;
        try {
            jasperReport= (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        } catch (JRException e) {
            AlertWindows.showErrorAlert("JRException", null, e.getMessage());
            return null;
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                AlertWindows.showErrorAlert("IOException", null, e.getMessage());
            }
        }

        return jasperPrint;
    }
}
