package com.developer.taxometer.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.developer.taxometer.models.Coordinate;
import com.developer.taxometer.models.Route;
import com.developer.taxometer.models.TariffModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by developer on 26.03.14.
 */
public class DataBaseOrmHelper extends OrmLiteSqliteOpenHelper {

    public static final int REFRESH_DATA_MODEL_IN_DATA_BASE = 1000;
    public static final int CREATE_DATA_MODEL_IN_DATA_BASE = 1001;

    private static final String DATABASE_NAME = "taximeter.db";
    private static final int DATABASE_VERSION = 1;

    private static final String LOG_TAG = DataBaseOrmHelper.class.getSimpleName();

    //dao links to objects data from db
    private DAORoute daoRoute;
    private DAOCoordinate daoCoordinate;
    private DAOTariffModel daoTariffModel;


    public DAOCoordinate getDaoCoordinate() throws java.sql.SQLException {
        if (daoCoordinate == null) {
            daoCoordinate = new DAOCoordinate(getConnectionSource(), Coordinate.class);
        }
        return daoCoordinate;
    }

    public DAOTariffModel getDaoTariffModel() throws java.sql.SQLException {
        if (daoTariffModel == null) {
            daoTariffModel = new DAOTariffModel(getConnectionSource(), TariffModel.class);
        }
        return daoTariffModel;
    }

    public DAORoute getDaoRoute() throws java.sql.SQLException {
        if (daoRoute == null) {
            daoRoute = new DAORoute(getConnectionSource(), Route.class);

        }
        return daoRoute;
    }


    public DataBaseOrmHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, TariffModel.class);
            TableUtils.createTable(connectionSource, Route.class);
            TableUtils.createTable(connectionSource, Coordinate.class);
        } catch (SQLException e) {
            Log.e(DataBaseOrmHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {

    }

    @Override
    public void close() {
        super.close();

        daoRoute = null;
        daoTariffModel = null;
        daoCoordinate = null;
    }

    public void setRouteInstanceToDataBase(int action_flag, Route routeModel) {

        try {

            daoRoute = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoRoute();
            if (action_flag == CREATE_DATA_MODEL_IN_DATA_BASE) {
                daoRoute.create(routeModel);
            }

            if (action_flag == REFRESH_DATA_MODEL_IN_DATA_BASE) {
                daoRoute.update(routeModel);
            }

        } catch (java.sql.SQLException e) {
            Log.e(DataBaseOrmHelper.class.getCanonicalName(), "Error set routeModel to db", e);
        }
    }

    /*try {

        daoRoute = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoRoute();
        daoRoute.create(currentRoute);
    } catch (
    java.sql.SQLException e) {
        Log.e(StartRouteActivity.class.getCanonicalName(), "daoRoute.create(currentRoute)", e);
    }*/

}
