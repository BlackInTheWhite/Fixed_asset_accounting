package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by User on 06.06.2018.
 */
public class Movements
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private SimpleStringProperty name;
    private Date date;
    private SimpleStringProperty number;
    private SimpleIntegerProperty type;
    private SimpleIntegerProperty oldDepartment;
    private SimpleIntegerProperty newDepartment;
    private SimpleDoubleProperty cost;
    private SimpleIntegerProperty oldMRP;
    private SimpleIntegerProperty newMRP;

    public double getCost() {
        return cost.get();
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
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

    public Movements(int id, int card, String name, Date date, String number, int type, int oldD, int newD, double cost, int oldMRP, int newMRP)
    {
        this.id=new SimpleIntegerProperty(id);
        this.cost=new SimpleDoubleProperty(cost);
        this.card=new SimpleIntegerProperty(card);
        this.name=new SimpleStringProperty(name);
        this.date=date;
        this.number=new SimpleStringProperty(number);
        this.type=new SimpleIntegerProperty(type);
        this.oldDepartment=new SimpleIntegerProperty(oldD);
        this.newDepartment=new SimpleIntegerProperty(newD);
        this.oldMRP=new SimpleIntegerProperty(oldMRP);
        this.newMRP=new SimpleIntegerProperty(newMRP);


    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public int getType() {
        return type.get();
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }

    public void setType(int type) {
        this.type.set(type);
    }

    public int getOldDepartment() {
        return oldDepartment.get();
    }

    public SimpleIntegerProperty oldDepartmentProperty() {
        return oldDepartment;
    }

    public void setOldDepartment(int oldDepartment) {
        this.oldDepartment.set(oldDepartment);
    }

    public int getNewDepartment() {
        return newDepartment.get();
    }

    public SimpleIntegerProperty newDepartmentProperty() {
        return newDepartment;
    }

    public void setNewDepartment(int newDepartment) {
        this.newDepartment.set(newDepartment);
    }

    public int getOldMRP() {
        return oldMRP.get();
    }

    public SimpleIntegerProperty oldMRPProperty() {
        return oldMRP;
    }

    public void setOldMRP(int oldMRP) {
        this.oldMRP.set(oldMRP);
    }

    public int getNewMRP() {
        return newMRP.get();
    }

    public SimpleIntegerProperty newMRPProperty() {
        return newMRP;
    }

    public void setNewMRP(int newMRP) {
        this.newMRP.set(newMRP);
    }
}
