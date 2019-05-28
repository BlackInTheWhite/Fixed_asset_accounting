package main.others.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 06.06.2018.
 */
public class DepreciationGroup
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty number;
    private SimpleStringProperty name;
    private SimpleStringProperty characteristic;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public DepreciationGroup(int id, String number, String name, String charac)
    {
        this.id=new SimpleIntegerProperty(id);

        this.number=new SimpleStringProperty(number);
        this.name=new SimpleStringProperty(name);
        this.characteristic=new SimpleStringProperty(charac);
    }

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCharacteristic() {
        return characteristic.get();
    }

    public SimpleStringProperty characteristicProperty() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic.set(characteristic);
    }
}
