package main.others.Utils;

import main.Fixed_asset_accounting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by User on 11.06.2018.
 */
public class CardChecker
{
    public static boolean checkCardWriteoff(int cardID)
    {
        Connection connection= Fixed_asset_accounting.getMyDatabase().getConnection();
        try
        {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery("select * from inventory_card where writeoff_date is not null " +
                    "and id_inventory_card="+cardID);
            if(rs.next())
            {
                statement.close();
                return true;
            }

            statement.close();

        }
        catch (SQLException e)
        {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
}
