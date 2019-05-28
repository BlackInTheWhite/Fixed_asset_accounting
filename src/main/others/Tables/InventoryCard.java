package main.others.Tables;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by User on 08.06.2018.
 */
public class InventoryCard {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty cardNumber;
    private SimpleStringProperty organization;
    private SimpleIntegerProperty department;
    private Date cardDate;
    private SimpleStringProperty objectName;
    private SimpleStringProperty OKUD;
    private SimpleStringProperty OKPO;
    private SimpleStringProperty OKOF;
    private SimpleIntegerProperty derpreciationGroupNumber;
    private SimpleStringProperty passportNumber;
    private SimpleStringProperty serialNumber;
    private SimpleStringProperty inventoryNumber;
    private Date deliveryDate;
    private Date writeoffDate;
    private SimpleStringProperty accountnumber;
    private SimpleStringProperty location;
    private SimpleStringProperty departmentNumber;
    private SimpleStringProperty manufacturer;
    private SimpleDoubleProperty initialCost;
    private SimpleIntegerProperty usefulLife;
    private SimpleIntegerProperty crp;
    private SimpleIntegerProperty mrp;
    private SimpleStringProperty participants;
    private SimpleIntegerProperty ownershipShare;
    private SimpleStringProperty structElemnts;
    private SimpleStringProperty mainObjectCharacteristics;
    private SimpleStringProperty deviceCharacteristics;
    private SimpleStringProperty notice;

    public InventoryCard(int id, int cardNumber, String organization, int department, Date cardDate,
                         String objectName, String OKUD, String OKPO, String OKOF, int depGroupNumber,
                         String passport, String serial, String inventory, Date delivery, Date writeoff,
                         String account, String location, String depNumber, String manufacturer,
                         double cost, int life, int crp, int mrp, String participants, int share,
                         String structElements, String mainObjectChar, String deviceChar, String notice)
    {
        this.id=new SimpleIntegerProperty(id);
        this.cardNumber=new SimpleIntegerProperty(cardNumber);
        this.organization=new SimpleStringProperty(organization);
        this.department=new SimpleIntegerProperty(department);
        this.cardDate=cardDate;
        this.objectName=new SimpleStringProperty(objectName);
        this.OKUD=new SimpleStringProperty(OKUD);
        this.OKPO=new SimpleStringProperty(OKPO);
        this.OKOF=new SimpleStringProperty(OKOF);
        this.derpreciationGroupNumber=new SimpleIntegerProperty(depGroupNumber);
        this.passportNumber=new SimpleStringProperty(passport);
        this.serialNumber=new SimpleStringProperty(serial);
        this.inventoryNumber=new SimpleStringProperty(inventory);
        this.deliveryDate=delivery;
        this.writeoffDate=writeoff;
        this.accountnumber=new SimpleStringProperty(account);
        this.location=new SimpleStringProperty(location);
        this.departmentNumber=new SimpleStringProperty(depNumber);
        this.manufacturer=new SimpleStringProperty(manufacturer);
        this.initialCost=new SimpleDoubleProperty(cost);
        this.usefulLife=new SimpleIntegerProperty(life);
        this.crp=new SimpleIntegerProperty(crp);
        this.mrp=new SimpleIntegerProperty(mrp);
        this.participants=new SimpleStringProperty(participants);
        this.ownershipShare=new SimpleIntegerProperty(share);
        this.structElemnts=new SimpleStringProperty(structElements);
        this.mainObjectCharacteristics=new SimpleStringProperty(mainObjectChar);
        this.deviceCharacteristics=new SimpleStringProperty(deviceChar);
        this.notice=new SimpleStringProperty(notice);

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

    public int getCardNumber() {
        return cardNumber.get();
    }

    public SimpleIntegerProperty cardNumberProperty() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public String getOrganization() {
        return organization.get();
    }

    public SimpleStringProperty organizationProperty() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization.set(organization);
    }

    public int getDepartment() {
        return department.get();
    }

    public SimpleIntegerProperty departmentProperty() {
        return department;
    }

    public void setDepartment(int department) {
        this.department.set(department);
    }

    public Date getCardDate() {
        return cardDate;
    }

    public void setCardDate(Date cardDate) {
        this.cardDate = cardDate;
    }

    public String getObjectName() {
        return objectName.get();
    }

    public SimpleStringProperty objectNameProperty() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName.set(objectName);
    }

    public String getOKUD() {
        return OKUD.get();
    }

    public SimpleStringProperty OKUDProperty() {
        return OKUD;
    }

    public void setOKUD(String OKUD) {
        this.OKUD.set(OKUD);
    }

    public String getOKPO() {
        return OKPO.get();
    }

    public SimpleStringProperty OKPOProperty() {
        return OKPO;
    }

    public void setOKPO(String OKPO) {
        this.OKPO.set(OKPO);
    }

    public String getOKOF() {
        return OKOF.get();
    }

    public SimpleStringProperty OKOFProperty() {
        return OKOF;
    }

    public void setOKOF(String OKOF) {
        this.OKOF.set(OKOF);
    }

    public int getDerpreciationGroupNumber() {
        return derpreciationGroupNumber.get();
    }

    public SimpleIntegerProperty derpreciationGroupNumberProperty() {
        return derpreciationGroupNumber;
    }

    public void setDerpreciationGroupNumber(int derpreciationGroupNumber) {
        this.derpreciationGroupNumber.set(derpreciationGroupNumber);
    }

    public String getPassportNumber() {
        return passportNumber.get();
    }

    public SimpleStringProperty passportNumberProperty() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber.set(passportNumber);
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public SimpleStringProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public String getInventoryNumber() {
        return inventoryNumber.get();
    }

    public SimpleStringProperty inventoryNumberProperty() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber.set(inventoryNumber);
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getWriteoffDate() {
        return writeoffDate;
    }

    public void setWriteoffDate(Date writeoffDate) {
        this.writeoffDate = writeoffDate;
    }

    public String getAccountnumber() {
        return accountnumber.get();
    }

    public SimpleStringProperty accountnumberProperty() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber.set(accountnumber);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getDepartmentNumber() {
        return departmentNumber.get();
    }

    public SimpleStringProperty departmentNumberProperty() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber.set(departmentNumber);
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public SimpleStringProperty manufacturerProperty() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public double getInitialCost() {
        return initialCost.get();
    }

    public SimpleDoubleProperty initialCostProperty() {
        return initialCost;
    }

    public void setInitialCost(double initialCost) {
        this.initialCost.set(initialCost);
    }

    public int getUsefulLife() {
        return usefulLife.get();
    }

    public SimpleIntegerProperty usefulLifeProperty() {
        return usefulLife;
    }

    public void setUsefulLife(int usefulLife) {
        this.usefulLife.set(usefulLife);
    }

    public int getCrp() {
        return crp.get();
    }

    public SimpleIntegerProperty crpProperty() {
        return crp;
    }

    public void setCrp(int crp) {
        this.crp.set(crp);
    }

    public int getMrp() {
        return mrp.get();
    }

    public SimpleIntegerProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp.set(mrp);
    }

    public String getParticipants() {
        return participants.get();
    }

    public SimpleStringProperty participantsProperty() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants.set(participants);
    }

    public int getOwnershipShare() {
        return ownershipShare.get();
    }

    public SimpleIntegerProperty ownershipShareProperty() {
        return ownershipShare;
    }

    public void setOwnershipShare(int ownershipShare) {
        this.ownershipShare.set(ownershipShare);
    }

    public String getStructElemnts() {
        return structElemnts.get();
    }

    public SimpleStringProperty structElemntsProperty() {
        return structElemnts;
    }

    public void setStructElemnts(String structElemnts) {
        this.structElemnts.set(structElemnts);
    }

    public String getMainObjectCharacteristics() {
        return mainObjectCharacteristics.get();
    }

    public SimpleStringProperty mainObjectCharacteristicsProperty() {
        return mainObjectCharacteristics;
    }

    public void setMainObjectCharacteristics(String mainObjectCharacteristics) {
        this.mainObjectCharacteristics.set(mainObjectCharacteristics);
    }

    public String getDeviceCharacteristics() {
        return deviceCharacteristics.get();
    }

    public SimpleStringProperty deviceCharacteristicsProperty() {
        return deviceCharacteristics;
    }

    public void setDeviceCharacteristics(String deviceCharacteristics) {
        this.deviceCharacteristics.set(deviceCharacteristics);
    }

    public String getNotice() {
        return notice.get();
    }

    public SimpleStringProperty noticeProperty() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice.set(notice);
    }
}
