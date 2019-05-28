package main.others.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 30.05.2018.
 */
public class Devices
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private SimpleStringProperty name;
    private SimpleIntegerProperty count;

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

    public Devices(int id, int card, String name, int count)
    {
        this.id=new SimpleIntegerProperty(id);
        this.card=new SimpleIntegerProperty(card);

        this.name=new SimpleStringProperty(name);
        this.count = new SimpleIntegerProperty(count);
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

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }
}
