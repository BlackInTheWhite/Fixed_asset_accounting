package main.others.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 06.06.2018.
 */
public class ResponsiblePerson
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty surname;
    private SimpleStringProperty name;
    private SimpleStringProperty patronymic;
    private SimpleStringProperty position;
    private SimpleStringProperty number;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public ResponsiblePerson(int id, String surname, String name, String patronymic, String position, String number)
    {
        this.id=new SimpleIntegerProperty(id);

        this.surname=new SimpleStringProperty(surname);
        this.name=new SimpleStringProperty(name);
        this.patronymic=new SimpleStringProperty(patronymic);
        this.position=new SimpleStringProperty(position);
        this.number=new SimpleStringProperty(number);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
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

    public String getPatronymic() {
        return patronymic.get();
    }

    public SimpleStringProperty patronymicProperty() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
    }

    public String getPosition() {
        return position.get();
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
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

    public String toString()
    {
        return surname.get()+" "+name.get().substring(0,1)+"."+patronymic.get().substring(0,1)+" ("+number.get()+")";
    }
}
