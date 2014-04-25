package com.developer.taxometer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;

import java.util.Locale;

/**
 * Created by developer on 14.04.14.
 */
public class LanguageHelper {
    private static final String ERROR_LOAD_LANGUAGE = "error_load";
    private static final String LANGUAGE_KEY_SAVE_TAG = "Language";

    public  static void loadLocale(Context context)
    {

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE, context.MODE_PRIVATE);
  String language = prefs.getString(context.getString(R.string.text_shared_pref_name_key_language_list), ERROR_LOAD_LANGUAGE);
     //   String language = prefs.getString(LANGUAGE_KEY_SAVE_TAG, ERROR_LOAD_LANGUAGE);
        changeLang(context,language);

    }

    public static void changeLang(Context context, String lang)
    {
     /*   if (lang.equalsIgnoreCase(ERROR_LOAD_LANGUAGE))
            return;*/
        TaximeterApplication.locale = new Locale(lang);
       //Locale myLocale = new Locale(lang);

      //  saveLocale(context,lang);
        Locale.setDefault(TaximeterApplication.locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = TaximeterApplication.locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        //updateTexts();
    }

    public static void saveLocale(Context context, String lang)
    {

        SharedPreferences prefs = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME_FILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_KEY_SAVE_TAG, lang);
        editor.commit();
    }
}
