package main.others.Utils;

/**
 * Created by User on 06.06.2018.
 */
public class MyKeyValueEx extends MyKeyValue
{
    private String idColumn;
    private String table;

    public MyKeyValueEx(int key, String value, String table, String idColumn) {
        super(key, value);
        this.table = table;
        this.idColumn=idColumn;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
