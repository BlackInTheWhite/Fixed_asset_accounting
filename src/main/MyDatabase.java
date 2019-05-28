package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.others.Utils.AlertWindows;
import main.others.Utils.MyKeyValue;
import main.others.Tables.ResponsiblePerson;

import java.sql.*;

/**
 * Created by User on 28.05.2018.
 */
public class MyDatabase
{
    private static final String URL="jdbc:mysql://localhost:3306/fixed_asset_accounting?autoReconnect=true&useSSL=false";
    private static final String DRIVER="com.mysql.jdbc.Driver";
    private static final String USER="root";
    private static final String PASSWORD="root";

    private static final String RP_STRING="%s %s.%s. (%s)";
    private static final String TABLE_ALL="select * from %s";
    private static final String TABLE="select %s, %s from %s";

    private static final String TYPE_TABLE="select * from %s where %s=\'%s\'";


    MyDatabase()
    {

    }

    public Connection getConnection()
    {
        Connection connection=null;
        try {
            try {
                Class.forName(DRIVER).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        catch (ClassNotFoundException e)
        {
            AlertWindows.showErrorAlert("ClassNotFoundException", null, e.getMessage());
        }
        try
        {
            connection=DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }

        return connection;
    }

    public ObservableList<MyKeyValue> getNotWriteOffCardList()
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("select id_inventory_card," +
                    "card_number,object_name from inventory_card where writeoff_date is null");

            while (rs.next())
            {
                list.add(new MyKeyValue(rs.getInt("id_inventory_card"),
                        rs.getString("object_name")+" ("+rs.getInt("card_number")+")"));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }

        return list;
    }
    public ObservableList<MyKeyValue> getOneCardAsList(int cardID)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select id_inventory_card," +
                    "card_number,object_name from inventory_card where id_inventory_card="+cardID);

            while (rs.next())
            {
                list.add(new MyKeyValue(rs.getInt("id_inventory_card"),
                        rs.getString("object_name")+" ("+rs.getInt("card_number")+")"));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return list;
    }
    public ObservableList<MyKeyValue> getDepartmentList()
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("select id_department_directory," +
                    "department_name from department_directory");

            while (rs.next())
            {
                list.add(new MyKeyValue(rs.getInt("id_department_directory"),
                        rs.getString("department_name")));
            }
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }

        return list;
    }
    public ObservableList<MyKeyValue> getResponsiblePersonList(String table)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery(String.format(TABLE_ALL, table));

            while (rs.next())
            {
                list.add(new MyKeyValue(rs.getInt(1),
                        String.format(RP_STRING, rs.getString("surname"), rs.getString("name").substring(0,1),
                                rs.getString("patronymic").substring(0,1), rs.getString("personnel_number"))));
            }
            rs.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public ObservableList<MyKeyValue> getOneRPAsList(String table, String column, int rpID)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery(String.format(TABLE_ALL, table)+" where "+column+"="+rpID);
            rs.next();
                list.add(new MyKeyValue(rs.getInt(1),
                        String.format(RP_STRING, rs.getString("surname"), rs.getString("name").substring(0,1),
                                rs.getString("patronymic").substring(0,1), rs.getString("personnel_number"))));
            rs.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public ObservableList<MyKeyValue> getOneDepartmentAsList(int departmentID)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select * from department_directory" +
                    " where id_department_directory="+departmentID);
            rs.next();

            list.add(new MyKeyValue(rs.getInt("id_department_directory"), rs.getString("department_name")));
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return list;
    }

    public ObservableList<MyKeyValue> getOneGroupAsList(int groupID)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select id_depreciation_group_directory, group_name from depreciation_group_directory" +
                    " where id_depreciation_group_directory="+groupID);
            rs.next();

            list.add(new MyKeyValue(rs.getInt("id_depreciation_group_directory"), rs.getString("group_name")));
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return list;
    }

    public ObservableList<MyKeyValue> getOneOperationAsList(String table, String idColumnName, String column, int id)
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select * from "+table+" where "+idColumnName+"="+id);
            rs.next();

            list.add(new MyKeyValue(rs.getInt(1), rs.getString(column)));
            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return list;
    }

    public Date getCardWriteOffDate(int cardID)
    {
        Connection connection = getConnection();

        Date date=null;

        try {

            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("select writeoff_date" +
                    " from inventory_card where id_inventory_card="+cardID);
            rs.next();
            date=rs.getDate("writeoff_date");

            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return date;
    }
    public MyKeyValue getCardDepartment(int cardID)
    {
        Connection connection = getConnection();

        MyKeyValue result=null;


        try {

            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("select id_department_directory, department_name from department_directory " +
                    "inner join inventory_card on department_directory.id_department_directory=inventory_card.department " +
                    "where inventory_card.id_inventory_card=" + cardID);
            rs.next();
            result=new MyKeyValue(rs.getInt("id_department_directory"), rs.getString("department_name"));

            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public MyKeyValue getCardMRP(int cardID)
    {
        Connection connection = getConnection();

        MyKeyValue result=null;

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select id_mrp, surname, name, patronymic, personnel_number from mrp " +
                    "inner join inventory_card on mrp.id_mrp=inventory_card.MRP " +
                    "where inventory_card.id_inventory_card=" + cardID);
            rs.next();
            result=new MyKeyValue(rs.getInt("id_mrp"),
                    String.format(RP_STRING, rs.getString("surname"),
                    rs.getString("name").substring(0,1), rs.getString("patronymic").substring(0,1),
                    rs.getString("personnel_number")));

            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public String getDepartmentByID(int departmentID)
    {
        Connection connection = getConnection();

        String result=null;

        try {

            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("select department_name from department_directory " +
                    "where id_department_directory=" + departmentID);
            rs.next();
            result=rs.getString("department_name");

            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public String getMRPByID(int idMRP)
    {
        Connection connection = getConnection();

        String result=null;

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery("select surname, name, patronymic, personnel_number from mrp " +
                    "where id_mrp=" + idMRP);
            rs.next();
            result=String.format(RP_STRING, rs.getString("surname"),
                    rs.getString("name").substring(0,1), rs.getString("patronymic").substring(0,1),
                    rs.getString("personnel_number"));

            rs.close();
            statement.close();
        }
        catch(SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public boolean checkForResponsiblePersonNumber(String number)
    {
        Connection connection=getConnection();
        boolean result=true;
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from crp where personnel_number=\'"+number.toLowerCase()+"\'");

            result=rs.next();

            rs=statement.executeQuery("select * from mrp where personnel_number='"+number.toLowerCase()+"\'");

            result|=rs.next();

            rs.close();
            statement.close();
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public boolean checkForType(String table, String column, String value)
    {
        Connection connection=getConnection();
        boolean result=true;
        try
        {
            Statement statement=connection.createStatement();

            ResultSet rs=statement.executeQuery(String.format(TYPE_TABLE, table, column,value.toLowerCase()));

            result=rs.next();

            rs.close();
            statement.close();
        }
        catch (SQLException e)
        {
            AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                AlertWindows.showErrorAlert("SQLException", null, e.getMessage());
            }
        }
        return result;
    }

    public ObservableList<MyKeyValue> getListFromDBTable(String table, String c1, String c2)
    {

        String sql=String.format(TABLE, c1,c2,table);
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MyKeyValue(rs.getInt(c1), rs.getString(c2)));
            }
            rs.close();
            ps.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public ObservableList<ResponsiblePerson> getRPFromDBTable(String table)
    {
        Connection connection = getConnection();
        ObservableList<ResponsiblePerson> list = FXCollections.observableArrayList();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs = statement.executeQuery(String.format(TABLE_ALL, table));

            while (rs.next())
            {
                list.add(new ResponsiblePerson(rs.getInt(1), rs.getString("surname"),
                        rs.getString("name"), rs.getString("patronymic"),
                        rs.getString("position"), rs.getString("personnel_number")));
            }
            rs.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public ObservableList<MyKeyValue> getCardListFromDBTable()
    {
        Connection connection = getConnection();
        ObservableList<MyKeyValue> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = connection.prepareStatement("select id_inventory_card," +
                    "card_number,object_name from inventory_card");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String temp=rs.getString("object_name")+" ("+rs.getInt("card_number")+")";
                list.add(new MyKeyValue(rs.getInt("id_inventory_card"), temp));
            }
            rs.close();
            ps.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
