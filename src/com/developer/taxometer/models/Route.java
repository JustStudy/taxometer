package com.developer.taxometer.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.developer.taxometer.database.DataBaseHelperFactory;
import com.developer.taxometer.utils.AppConstants;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


@DatabaseTable(tableName = "route_")
public class Route implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.STRING)
    private String nameRoute;

    @DatabaseField(foreign = true)
    private TariffModel tariffModel;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double totalPrice;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double routeDistance;

    @DatabaseField(dataType = DataType.LONG)
    private long routeTime;

    @DatabaseField(dataType = DataType.LONG)
     private long waitingTime;

    @DatabaseField(dataType = DataType.DOUBLE)
       private double cashForAdditionServices;

    @ForeignCollectionField(eager = true)
    private Collection<Coordinate> coordinatesList = new ArrayList<Coordinate>();


    public void addCoordinate(Coordinate coordinate) {
         coordinate.setIdRoute(id);

        try {
            DataBaseHelperFactory.getDataBaseOrmHelper().getDaoCoordinate().create(coordinate);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        coordinatesList.add(coordinate);
    }

    public void removeCoordinate(Coordinate coordinate) {
        coordinatesList.remove(coordinate);

        try {
            DataBaseHelperFactory.getDataBaseOrmHelper().getDaoCoordinate().delete(coordinate);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public double getCashForAdditionServices() {
        return cashForAdditionServices;
    }

    public void setCashForAdditionServices(double cashForAdditionServices) {
        this.cashForAdditionServices = cashForAdditionServices;
    }

    public TariffModel getTariffModel() {
        return tariffModel;
    }

    public void setTariffModel(TariffModel tariffModel) {
        this.tariffModel = tariffModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRoute() {
        return nameRoute;
    }

    public void setNameRoute(String nameRoute) {
        this.nameRoute = nameRoute;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getRouteDistance() {
        return routeDistance;
    }

    public void setRouteDistance(double routeDistance) {
        this.routeDistance = routeDistance;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public long getRouteTime() {
        return routeTime;
    }

    public void setRouteTime(long routeTime) {
        this.routeTime = routeTime;
    }

    public Route (){}


    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public Route(Parcel in) {
        id = in.readInt();
        nameRoute = in.readString();
        Bundle tariffBundle = in.readBundle();
        tariffBundle.setClassLoader(TariffModel.class.getClassLoader());
        tariffModel = tariffBundle.getParcelable(AppConstants.MODEL_TARIFF_PARCELABLE_KEY);
        totalPrice = in.readDouble();
        cashForAdditionServices = in.readDouble();
        routeDistance = in.readDouble();
        waitingTime = in.readLong();
        routeTime = in.readLong();

        ArrayList<Coordinate> co = new ArrayList<Coordinate>();
         in.readList(co, Coordinate.class.getClassLoader());
        coordinatesList.addAll(co);

    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(id);
        out.writeString(nameRoute);
        Bundle tariffWriteBundle = new Bundle();
        tariffWriteBundle.putParcelable(AppConstants.MODEL_TARIFF_PARCELABLE_KEY,tariffModel);
        out.writeBundle(tariffWriteBundle);
        out.writeDouble(totalPrice);
        out.writeDouble(cashForAdditionServices);
        out.writeDouble(routeDistance);
        out.writeLong(waitingTime);
        out.writeLong(routeTime);

        out.writeTypedList(new ArrayList<Parcelable>(coordinatesList));
        out.writeList(new ArrayList(coordinatesList));

    }
}
