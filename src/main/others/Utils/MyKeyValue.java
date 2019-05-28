package main.others.Utils;

/**
 * Created by User on 29.05.2018.
 */
public class MyKeyValue
{
    private int key;
    private String value;

    public MyKeyValue(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {

        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
