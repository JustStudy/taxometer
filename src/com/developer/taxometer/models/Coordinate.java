package com.developer.taxometer.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.developer.taxometer.utils.AppConstants;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "coordinate")
public class Coordinate implements Parcelable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.INTEGER)
    private int idRoute;

    @DatabaseField(dataType = DataType.INTEGER)
    private int obtainTime;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double latitude;

    @DatabaseField(dataType = DataType.DOUBLE)
    private double longitude;

   @DatabaseField(foreign = true, foreignAutoRefresh = true,foreignAutoCreate = true)
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }


    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public int getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(int obtainTime) {
        this.obtainTime = obtainTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinate (){}


    public static final Parcelable.Creator<Coordinate> CREATOR = new Parcelable.Creator<Coordinate>() {
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public Coordinate(Parcel in) {
         id = in.readInt();
        idRoute = in.readInt();
        obtainTime = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        Bundle routeBundle = in.readBundle();
        routeBundle.setClassLoader(Route.class.getClassLoader());
        route = routeBundle.getParcelable(AppConstants.MODEL_ROUTE_BASE_PARCELABLE_KEY);


    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(id);
        out.writeInt(idRoute);
        out.writeInt(obtainTime);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        Bundle routeParseBundle = new Bundle();
        routeParseBundle.putParcelable(AppConstants.MODEL_ROUTE_BASE_PARCELABLE_KEY,route);
        out.writeBundle(routeParseBundle);

    }

}
