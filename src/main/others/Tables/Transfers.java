package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by User on 09.06.2018.
 */
public class Transfers
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty recipient;
    private SimpleStringProperty contacts;
    private SimpleStringProperty basis;
    private SimpleStringProperty name;
    private SimpleStringProperty number;
    private Date date;
    private SimpleDoubleProperty cost;

    public Transfers(int id, String recipient, String contacts, String basis, String name, String number, Date date, double cost)
    {
        this.id=new SimpleIntegerProperty(id);
        this.recipient=new SimpleStringProperty(recipient);
        this.contacts=new SimpleStringProperty(contacts);
        this.basis=new SimpleStringProperty(basis);
        this.name=new SimpleStringProperty(name);
        this.number=new SimpleStringProperty(number);
        this.date=date;
        this.cost=new SimpleDoubleProperty(cost);
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

    public String getRecipient() {
        return recipient.get();
    }

    public SimpleStringProperty recipientProperty() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient.set(recipient);
    }

    public String getContacts() {
        return contacts.get();
    }

    public SimpleStringProperty contactsProperty() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts.set(contacts);
    }

    public String getBasis() {
        return basis.get();
    }

    public SimpleStringProperty basisProperty() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis.set(basis);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
