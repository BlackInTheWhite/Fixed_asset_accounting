package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by User on 04.06.2018.
 */
public class Revaluation
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private Date date;
    private SimpleDoubleProperty coefficient, cost;

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

    public Revaluation(int id, int card, Date date, Double coeff, Double cost)
    {
        this.id=new SimpleIntegerProperty(id);
        this.card=new SimpleIntegerProperty(card);

        this.date= date;
        this.coefficient=new SimpleDoubleProperty(coeff);
        this.cost=new SimpleDoubleProperty(cost);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCoefficient() {
        return coefficient.get();
    }

    public SimpleDoubleProperty coefficientProperty() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient.set(coefficient);
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
