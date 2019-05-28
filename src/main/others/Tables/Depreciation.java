package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by User on 05.06.2018.
 */
public class Depreciation
{
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty card;
    private SimpleIntegerProperty year;
    private SimpleDoubleProperty m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;

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

    public Depreciation(int id, int card, int year, double m1, double m2, double m3, double m4, double m5, double m6, double m7,
                        double m8, double m9, double m10, double m11, double m12)
    {
        this.id=new SimpleIntegerProperty(id);

        this.year=new SimpleIntegerProperty(year);
        this.card=new SimpleIntegerProperty(card);

        this.m1=new SimpleDoubleProperty(m1);
        this.m2=new SimpleDoubleProperty(m2);
        this.m3=new SimpleDoubleProperty(m3);
        this.m4=new SimpleDoubleProperty(m4);
        this.m5=new SimpleDoubleProperty(m5);
        this.m6=new SimpleDoubleProperty(m6);
        this.m7=new SimpleDoubleProperty(m7);
        this.m8=new SimpleDoubleProperty(m8);
        this.m9=new SimpleDoubleProperty(m9);
        this.m10=new SimpleDoubleProperty(m10);
        this.m11=new SimpleDoubleProperty(m11);
        this.m12=new SimpleDoubleProperty(m12);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public double getM1() {
        return m1.get();
    }

    public SimpleDoubleProperty m1Property() {
        return m1;
    }

    public void setM1(double m1) {
        this.m1.set(m1);
    }

    public double getM2() {
        return m2.get();
    }

    public SimpleDoubleProperty m2Property() {
        return m2;
    }

    public void setM2(double m2) {
        this.m2.set(m2);
    }

    public double getM3() {
        return m3.get();
    }

    public SimpleDoubleProperty m3Property() {
        return m3;
    }

    public void setM3(double m3) {
        this.m3.set(m3);
    }

    public double getM4() {
        return m4.get();
    }

    public SimpleDoubleProperty m4Property() {
        return m4;
    }

    public void setM4(double m4) {
        this.m4.set(m4);
    }

    public double getM5() {
        return m5.get();
    }

    public SimpleDoubleProperty m5Property() {
        return m5;
    }

    public void setM5(double m5) {
        this.m5.set(m5);
    }

    public double getM6() {
        return m6.get();
    }

    public SimpleDoubleProperty m6Property() {
        return m6;
    }

    public void setM6(double m6) {
        this.m6.set(m6);
    }

    public double getM7() {
        return m7.get();
    }

    public SimpleDoubleProperty m7Property() {
        return m7;
    }

    public void setM7(double m7) {
        this.m7.set(m7);
    }

    public double getM8() {
        return m8.get();
    }

    public SimpleDoubleProperty m8Property() {
        return m8;
    }

    public void setM8(double m8) {
        this.m8.set(m8);
    }

    public double getM9() {
        return m9.get();
    }

    public SimpleDoubleProperty m9Property() {
        return m9;
    }

    public void setM9(double m9) {
        this.m9.set(m9);
    }

    public double getM10() {
        return m10.get();
    }

    public SimpleDoubleProperty m10Property() {
        return m10;
    }

    public void setM10(double m10) {
        this.m10.set(m10);
    }

    public double getM11() {
        return m11.get();
    }

    public SimpleDoubleProperty m11Property() {
        return m11;
    }

    public void setM11(double m11) {
        this.m11.set(m11);
    }

    public double getM12() {
        return m12.get();
    }

    public SimpleDoubleProperty m12Property() {
        return m12;
    }

    public void setM12(double m12) {
        this.m12.set(m12);
    }
}
