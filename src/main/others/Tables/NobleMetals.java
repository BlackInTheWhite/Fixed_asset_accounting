package main.others.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 30.05.2018.
 */
public class NobleMetals
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private SimpleStringProperty name;
    private SimpleStringProperty number;
    private SimpleStringProperty unit;
    private SimpleIntegerProperty count;
    private SimpleIntegerProperty mass;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCard() {
        return card.get();
    }

    public SimpleIntegerProperty cardProperty() {
        return card;
    }

    public void setCard(int card) {
        this.card.set(card);
    }

    public NobleMetals(int id, int card, String name, String number, String unit, int count, int mass)
    {
        this.id=new SimpleIntegerProperty(id);
        this.card=new SimpleIntegerProperty(card);
        this.name=new SimpleStringProperty(name);
        this.number=new SimpleStringProperty(number);
        this.unit=new SimpleStringProperty(unit);
        this.count=new SimpleIntegerProperty(count);
        this.mass=new SimpleIntegerProperty(mass);
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

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public int getMass() {
        return mass.get();
    }

    public SimpleIntegerProperty massProperty() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass.set(mass);
    }
}
