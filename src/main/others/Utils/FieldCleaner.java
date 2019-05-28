package main.others.Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Created by User on 31.05.2018.
 */
public class FieldCleaner
{
    public static void clear(TextField... fields)
    {
        for(TextField tf:fields)
            tf.clear();
    }

    public static void clear(ComboBox... boxes)
    {
        for(ComboBox tf:boxes)
            tf.getSelectionModel().clearSelection();
    }

    public static void clear(DatePicker ... pickers)
    {
        for(DatePicker dp:pickers)
            dp.setValue(null);
    }
}
