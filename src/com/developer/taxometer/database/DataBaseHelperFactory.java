package com.developer.taxometer.database;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by developer on 26.03.14.
 */
public class DataBaseHelperFactory {

    private static DataBaseOrmHelper dataBaseOrmHelper;

    public static void setDataBaseOrmHelper(Context context) {
        dataBaseOrmHelper = OpenHelperManager.getHelper(context, DataBaseOrmHelper.class);
    }

    public static DataBaseOrmHelper getDataBaseOrmHelper() {
        return dataBaseOrmHelper;
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        dataBaseOrmHelper = null;
    }
}
