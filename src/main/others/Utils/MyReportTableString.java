package main.others.Utils;


/**
 * Created by User on 16.06.2018.
 */
public class MyReportTableString
{
    private String reportName;
    private String tableName;

    public MyReportTableString(String report, String table)
    {
        reportName=report;
        tableName=table;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String toString()
    {
        return tableName;
    }
}
