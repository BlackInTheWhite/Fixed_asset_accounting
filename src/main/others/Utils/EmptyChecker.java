package main.others.Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Created by User on 31.05.2018.
 */
public class EmptyChecker
{
    public static boolean isAnyEmpty(TextField ... fields)
    {
        for(TextField tf:fields)
            if(tf.getText().isEmpty())
                return true;
        return false;
    }
    public static boolean isAnyEmpty(ComboBox ... boxes)
    {
        for(ComboBox cb:boxes)
            if(cb.getSelectionModel().getSelectedIndex()==-1)
                return true;
        return false;
    }
    public static boolean isAnyEmpty(DatePicker ... pickers)
    {
        for(DatePicker dp:pickers)
            if(dp.getValue()==null)
                    return true;
        return false;
    }
}
