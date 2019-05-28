package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by User on 09.06.2018.
 */
public class Transmission
{
    private SimpleIntegerProperty id;
    private Date productionDate;
    private Date repairDate;
    private SimpleStringProperty name;
    private SimpleStringProperty number;
    private Date date;
    private SimpleIntegerProperty period;
    private SimpleDoubleProperty depreciation;
    private SimpleDoubleProperty residualCost;

    public Transmission(int id, Date productionDate, Date repairDate, String name, String number,
                        Date date, int period, double depreciation, double residual)
    {
        this.id=new SimpleIntegerProperty(id);
        this.productionDate=productionDate;
        this.repairDate=repairDate;
        this.name=new SimpleStringProperty(name);
        this.number=new SimpleStringProperty(number);
        this.date=date;
        this.period=new SimpleIntegerProperty(period);
        this.depreciation=new SimpleDoubleProperty(depreciation);
        this.residualCost=new SimpleDoubleProperty(residual);
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

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
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

    public int getPeriod() {
        return period.get();
    }

    public SimpleIntegerProperty periodProperty() {
        return period;
    }

    public void setPeriod(int period) {
        this.period.set(period);
    }

    public double getDepreciation() {
        return depreciation.get();
    }

    public SimpleDoubleProperty depreciationProperty() {
        return depreciation;
    }

    public void setDepreciation(double depreciation) {
        this.depreciation.set(depreciation);
    }

    public double getResidualCost() {
        return residualCost.get();
    }

    public SimpleDoubleProperty residualCostProperty() {
        return residualCost;
    }

    public void setResidualCost(double residualCost) {
        this.residualCost.set(residualCost);
    }
}
