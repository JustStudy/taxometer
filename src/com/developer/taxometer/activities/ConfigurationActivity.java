package com.developer.taxometer.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.widget.Toast;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.services.LocationService;
import com.developer.taxometer.utils.LanguageHelper;
import net.saik0.android.unifiedpreference.UnifiedPreferenceFragment;
import net.saik0.android.unifiedpreference.UnifiedSherlockPreferenceActivity;


/**
 * Created by developer on 24.03.14.
 */
public class ConfigurationActivity extends UnifiedSherlockPreferenceActivity {


    static ListPreference listPreferenceLanguage = null;
    static ListPreference listPreferenceFrequencyGps = null;
    static ListPreference listPreferenceDistanceGps = null;
    static ListPreference listPreferenceCurrency = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ////for language
        LanguageHelper.loadLocale(this);


        setHeaderRes(R.xml.preference_headers);
        setSharedPreferencesMode(Context.MODE_PRIVATE);
        setSharedPreferencesName("selected_preference");


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (isSinglePane()) {
            // Set a listener for the activity
            initGeneralPreferences();
        }
    }


    private void initGeneralPreferences() {
        listPreferenceLanguage = (ListPreference) findPreference(getString(R.string.text_shared_pref_name_key_language_list));
        listPreferenceLanguage.setOnPreferenceChangeListener(languagePreferenceChangeListener);
        listPreferenceDistanceGps = (ListPreference) findPreference(getString(R.string.text_shared_pref_name_key_distance_gps_list));
        listPreferenceDistanceGps.setOnPreferenceChangeListener(distanceGpsChangeListener);
        listPreferenceFrequencyGps = (ListPreference) findPreference(getString(R.string.text_shared_pref_name_key_frequency_gps_list));
        listPreferenceFrequencyGps.setOnPreferenceChangeListener(frequencyGpsChangeListener);
        listPreferenceCurrency = (ListPreference) findPreference(getString(R.string.text_shared_pref_name_key_money_list));
        listPreferenceCurrency.setOnPreferenceChangeListener(currencyListChangeListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GeneralPreferenceFragment extends UnifiedPreferenceFragment {

    }

    public static class GPSPreferenceFragment extends UnifiedPreferenceFragment {
    }


    private static Preference.OnPreferenceChangeListener languagePreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            //listPreferenceLanguage.setSummary(listPreferenceLanguage.getEntry());
            CharSequence[] entryValuesArray = listPreferenceLanguage.getEntryValues();
            CharSequence[] entryArray = listPreferenceLanguage.getEntries();

            for (int i = 0; i < entryValuesArray.length; i++) {
                if (entryValuesArray[i].toString().equals(o.toString())) {
                    listPreferenceLanguage.setSummary(entryArray[i].toString());
                    break;
                }
            }


            // LanguageHelper.saveLocale(TaximeterApplication.appContext, o.toString());
            LanguageHelper.changeLang(TaximeterApplication.appContext, o.toString());
            //    Toast.makeText(preference.getContext(), "summary  " + preference.getSummary() + " o " + o.toString() + " order " + preference.getOrder() + " key:" + preference.getKey() + " clicked", Toast.LENGTH_LONG).show();

            return true;
        }
    };

    Preference.OnPreferenceChangeListener frequencyGpsChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {


            CharSequence[] entryValuesArray = listPreferenceFrequencyGps.getEntryValues();
            CharSequence[] entryArray = listPreferenceFrequencyGps.getEntries();

            for (int i = 0; i < entryValuesArray.length; i++) {
                if (entryValuesArray[i].toString().equals(o.toString())) {
                    listPreferenceFrequencyGps.setSummary(entryArray[i].toString());
                    break;
                }
            }


            Toast.makeText(TaximeterApplication.appContext, "frequency " + listPreferenceFrequencyGps.getSummary().toString(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    Preference.OnPreferenceChangeListener distanceGpsChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            CharSequence[] entryValuesArray = listPreferenceDistanceGps.getEntryValues();
            CharSequence[] entryArray = listPreferenceDistanceGps.getEntries();

            for (int i = 0; i < entryValuesArray.length; i++) {
                if (entryValuesArray[i].toString().equals(o.toString())) {
                    listPreferenceDistanceGps.setSummary(entryArray[i].toString());
                    break;
                }
            }


            Toast.makeText(TaximeterApplication.appContext, "distance " + listPreferenceDistanceGps.getSummary().toString(), Toast.LENGTH_SHORT).show();

            return true;
        }
    };

    Preference.OnPreferenceChangeListener currencyListChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            CharSequence[] entryValuesArray = listPreferenceCurrency.getEntryValues();
            CharSequence[] entryArray = listPreferenceCurrency.getEntries();

            for (int i = 0; i < entryValuesArray.length; i++) {
                if (entryValuesArray[i].toString().equals(o.toString())) {
                    listPreferenceCurrency.setSummary(entryArray[i].toString());

                    if(LocationService.locationService!=null){

                        LocationService.locationService.setTaxiCurrency(o.toString());
                    }
                    break;
                }
            }


            Toast.makeText(TaximeterApplication.appContext, "currency " + listPreferenceCurrency.getSummary().toString(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
