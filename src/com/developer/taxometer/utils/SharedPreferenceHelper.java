package com.developer.taxometer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.developer.taxometer.models.TariffModel;

/**
 * Created by developer on 28.03.14.
 */
public class SharedPreferenceHelper {
private Context context;
   public SharedPreferenceHelper (Context context){
        this.context = context;
    }


  public  TariffModel  readDefaultTariffFromSharedPref(){
        SharedPreferences defaultTariffSharePref = PreferenceManager.getDefaultSharedPreferences(context);
        TariffModel tariffModel = new TariffModel();
        boolean isDefaultAvailable = defaultTariffSharePref.getBoolean(AppConstants.IS_DEFAULT_TARIFF_AVAILABLE,false);
        if(isDefaultAvailable){

            tariffModel.setNameTariff(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_NAME_TARIFF,"Error"));
            tariffModel.setLuggageTransportationPrice(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_LUGGAGE,"0")));
            tariffModel.setStartPayMoney(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_START_PRICE,"0")));
            tariffModel.setAnimalTransportationPrice(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_ANIMAL_PRICE,"0")));
            tariffModel.setMoneyPerKm(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_PRICE_PER_KM,"0")));
            tariffModel.setSmokeServicePrice(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_SMOKE_PRICE,"0")));
            tariffModel.setChildChairServicePrice(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_CHILD_CHAIR_PRICE,"0")));
            tariffModel.setOtherServicePrice(Double.valueOf(defaultTariffSharePref.getString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_OTHERS_SERVICES_PRICE,"0")));
            return  tariffModel;

        } else {
            return null;
        }

    }

  public static void writeDefaultTariffToSharedPref(Context staticContext, TariffModel tariffModel){

        SharedPreferences writeDefTariff = PreferenceManager.getDefaultSharedPreferences(staticContext);
        SharedPreferences.Editor editor = writeDefTariff.edit();
        editor.putBoolean(AppConstants.IS_DEFAULT_TARIFF_AVAILABLE, true);
        editor.putString(AppConstants.SHARED_PREF_DEFAULT_NAME_TARIFF,tariffModel.getNameTariff());

        editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_PRICE_PER_KM,
                String.valueOf(tariffModel.getMoneyPerKm()));
        editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_START_PRICE,
                String.valueOf(tariffModel.getStartPayMoney()));
        editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_LUGGAGE,
                String.valueOf(tariffModel.getLuggageTransportationPrice()));
        editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_ANIMAL_PRICE,
                String.valueOf(tariffModel.getAnimalTransportationPrice()));
      editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_SMOKE_PRICE,
              String.valueOf(tariffModel.getSmokeServicePrice()));
      editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_CHILD_CHAIR_PRICE,
              String.valueOf(tariffModel.getChildChairServicePrice()));
      editor.putString(AppConstants.SHARED_PREF_DEFAULT_TARIFF_OTHERS_SERVICES_PRICE,
              String.valueOf(tariffModel.getOtherServicePrice()));

        editor.commit();

    }
}
