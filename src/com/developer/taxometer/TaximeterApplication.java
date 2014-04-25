package com.developer.taxometer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.developer.taxometer.database.DataBaseHelperFactory;
import com.developer.taxometer.utils.LanguageHelper;

import java.util.Locale;

/**
 * Created by developer on 24.03.14.
 */
public class TaximeterApplication extends Application {

    public static boolean IS_SHOW_DIALOG_TARIFFS = true;
    public static Context appContext;
    public static Locale locale;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        DataBaseHelperFactory.setDataBaseOrmHelper(appContext);
        LanguageHelper.loadLocale(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DataBaseHelperFactory.releaseHelper();
        appContext = null;
    }



}
