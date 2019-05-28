package main.others.Utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 06.06.2018.
 */
public class MyType
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty type;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public MyType(int id, String type)
    {
        this.id=new SimpleIntegerProperty(id);

        this.type=new SimpleStringProperty(type);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
