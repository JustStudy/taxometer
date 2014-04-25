package com.developer.taxometer.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tariff")
public class TariffModel implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double moneyPerKm = 0;

    @DatabaseField(columnName = "name", dataType = DataType.STRING)
    private String nameTariff;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double startPayMoney = 0;

    @DatabaseField(dataType = DataType.STRING)
    private String tariffIcon;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double animalTransportationPrice = 0;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double luggageTransportationPrice = 0;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double smokeServicePrice = 0;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double childChairServicePrice = 0;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double otherServicePrice = 0;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double totalPrice = 0;

    private boolean luggageIsTransport;

    private boolean animalIsTransport;

    private boolean isSmokeService;
    private  boolean isChildService;
    private boolean isOtherService;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getSmokeServicePrice() {
        return smokeServicePrice;
    }

    public void setSmokeServicePrice(double smokeServicePrice) {
        this.smokeServicePrice = smokeServicePrice;
    }

    public double getChildChairServicePrice() {
        return childChairServicePrice;
    }

    public void setChildChairServicePrice(double childChairServicePrice) {
        this.childChairServicePrice = childChairServicePrice;
    }

    public double getOtherServicePrice() {
        return otherServicePrice;
    }

    public void setOtherServicePrice(double otherServicePrice) {
        this.otherServicePrice = otherServicePrice;
    }

    public boolean isSmokeService() {
        return isSmokeService;
    }

    public void setSmokeService(boolean isSmokeService) {
        this.isSmokeService = isSmokeService;
    }

    public boolean isChildService() {
        return isChildService;
    }

    public void setChildService(boolean isChildService) {
        this.isChildService = isChildService;
    }

    public boolean isOtherService() {
        return isOtherService;
    }

    public void setOtherService(boolean isOtherService) {
        this.isOtherService = isOtherService;
    }

    public double getAnimalTransportationPrice() {
        return animalTransportationPrice;
    }

    public void setAnimalTransportationPrice(double animalTransportationPrice) {
        this.animalTransportationPrice = animalTransportationPrice;
    }

    public double getLuggageTransportationPrice() {
        return luggageTransportationPrice;
    }

    public void setLuggageTransportationPrice(double luggageTransportationPrice) {
        this.luggageTransportationPrice = luggageTransportationPrice;
    }

    public boolean isLuggageIsTransport() {
        return luggageIsTransport;
    }

    public void setLuggageIsTransport(boolean luggageIsTransport) {
        this.luggageIsTransport = luggageIsTransport;
    }

    public boolean isAnimalIsTransport() {
        return animalIsTransport;
    }

    public void setAnimalIsTransport(boolean animalIsTransport) {
        this.animalIsTransport = animalIsTransport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTariffIcon() {
        return tariffIcon;
    }

    public void setTariffIcon(String tariffIcon) {
        this.tariffIcon = tariffIcon;
    }

    public double getMoneyPerKm() {
        return moneyPerKm;
    }

    public void setMoneyPerKm(double moneyPerKm) {
        this.moneyPerKm = moneyPerKm;
    }

    public String getNameTariff() {
        return nameTariff;
    }

    public void setNameTariff(String nameTariff) {
        this.nameTariff = nameTariff;
    }

    public double getStartPayMoney() {
        return startPayMoney;
    }

    public void setStartPayMoney(double startPayMoney) {
        this.startPayMoney = startPayMoney;
    }


    public TariffModel() {

    }

    public TariffModel(Parcel in) {

        id = in.readInt();
        moneyPerKm = in.readDouble();
        nameTariff = in.readString();
        startPayMoney = in.readDouble();
        tariffIcon = in.readString();
        animalTransportationPrice = in.readDouble();
        luggageTransportationPrice = in.readDouble();
        smokeServicePrice = in.readDouble();
        childChairServicePrice = in.readDouble();
        otherServicePrice = in.readDouble();
        totalPrice = in.readDouble();

        animalIsTransport = in.readByte() != 0;
        luggageIsTransport = in.readByte() != 0;
        isSmokeService = in.readByte() != 0;
        isChildService = in.readByte() != 0;
        isOtherService = in.readByte() != 0;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(id);
        out.writeDouble(moneyPerKm);
        out.writeString(nameTariff);
        out.writeDouble(startPayMoney);
        out.writeString(tariffIcon);

        out.writeDouble(animalTransportationPrice);
        out.writeDouble(luggageTransportationPrice);
        out.writeDouble(smokeServicePrice);
        out.writeDouble(childChairServicePrice);
        out.writeDouble(otherServicePrice);
        out.writeDouble(totalPrice);

        out.writeByte((byte) (animalIsTransport ? 1 : 0));
        out.writeByte((byte) (luggageIsTransport ? 1 : 0));
        out.writeByte((byte) (isSmokeService ? 1 : 0));
        out.writeByte((byte) (isChildService ? 1 : 0));
        out.writeByte((byte) (isOtherService ? 1 : 0));


    }

    public static final Parcelable.Creator<TariffModel> CREATOR = new Parcelable.Creator<TariffModel>() {
        public TariffModel createFromParcel(Parcel in) {
            return new TariffModel(in);
        }

        public TariffModel[] newArray(int size) {
            return new TariffModel[size];
        }
    };
}
