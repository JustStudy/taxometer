package com.developer.taxometer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.developer.taxometer.R;

/**
 * Created by developer on 14.04.14.
 */
public class PreferenceHelper {

    private static final String ERROR_DATA_LOAD = "ERROR_DATA_LOAD";
    private static final String FREQUENCY_KEY_SAVE_TAG = "FREQUENCY_KEY_SAVE_TAG";
    private static final String DISTANCE_KEY_SAVE_TAG = "DISTANCE_KEY_SAVE_TAG";



    public static int readUpdateGpsTimeFrequency(Context context){
        SharedPreferences updateGpsSharedPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE,Context.MODE_PRIVATE);
        String value = updateGpsSharedPref.getString(context.getString(R.string.text_shared_pref_name_key_frequency_gps_list),String.valueOf(AppConstants.MIN_FREQUENCY_VALUE_TO_UPDATE_GPS_STATUS));
        int timeFrequency = Integer.valueOf(value);
        return timeFrequency;
    }
    public static void saveUpdateGpsTime(Context context, int value){

        SharedPreferences prefs = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(FREQUENCY_KEY_SAVE_TAG, value);
        editor.commit();

    }

    public static float readUpdateGpsDistance(Context context){
        SharedPreferences distanceGpsSharedPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE,Context.MODE_PRIVATE);
      String value = distanceGpsSharedPref.getString(context.getString(R.string.text_shared_pref_name_key_distance_gps_list), String.valueOf(AppConstants.MIN_DISTANCE_TO_UPDATE_GPS_STATUS));
        float timeFrequency = Float.valueOf(value);
        return timeFrequency;

    }

    public static void saveUpdateGpsDistance(Context context,float value){
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(DISTANCE_KEY_SAVE_TAG, value);
        editor.commit();

    }

public static String readCurrencyFromPreference(Context context) {

    SharedPreferences currencyPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE,Context.MODE_PRIVATE);
    String currency = currencyPref.getString(context.getString(R.string.text_shared_pref_name_key_money_list),AppConstants.DEFAULT_APP_CURRENCY);
    return currency;
}

}
