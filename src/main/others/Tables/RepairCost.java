package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by User on 09.06.2018.
 */
public class RepairCost
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private SimpleIntegerProperty type;
    private SimpleStringProperty name;
    private Date date;
    private SimpleStringProperty number;

    private SimpleDoubleProperty cost;

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

    public int getType() {
        return type.get();
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }

    public void setType(int type) {
        this.type.set(type);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public double getCost() {
        return cost.get();
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public RepairCost(int id, int card, int type, String name, Date date, String number, double cost)
    {
        this.name=new SimpleStringProperty(name);
        this.card=new SimpleIntegerProperty(card);

        this.type=new SimpleIntegerProperty(type);
        this.id=new SimpleIntegerProperty(id);
        this.date=date;
        this.number=new SimpleStringProperty(number);

        this.cost=new SimpleDoubleProperty(cost);

    }
}
