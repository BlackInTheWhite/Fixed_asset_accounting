package main.others.Utils;

import javafx.scene.control.TextField;

import java.util.regex.Pattern;

/**
 * Created by User on 29.05.2018.
 */
public class FieldPattern
{
    public static Pattern onlyNumbersPattern=Pattern.compile("\\d{0,10}");
    public static Pattern onlyDecimalPattern=Pattern.compile("^([0-9]{0,9}(\\.[0-9]{0,2})?)?$");
    public static Pattern onlyLettersPattern=Pattern.compile("^([A-Za-zА-Яа-я][A-Za-zА-Яа-я ]*)?$");
    public static Pattern numbersLettersPattern=Pattern.compile("^([A-Za-zА-Яа-я0-9][A-Za-zА-Яа-я0-9 ]*)?$");
    public static Pattern year=Pattern.compile("\\d{0,4}");
    public static void setPatternToFieldProperty(Pattern pattern, TextField ... fields)
    {
        for(TextField tf:fields)
            tf.textProperty().addListener((observable, oldValue, newValue) ->
            {
                if(!pattern.matcher(newValue).matches()) tf.setText(oldValue);
            });
    }

}
