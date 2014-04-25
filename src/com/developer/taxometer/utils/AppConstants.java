package com.developer.taxometer.utils;

/**
 * Created by developer on 24.03.14.
 */
public class AppConstants {

    public static int MIN_FREQUENCY_VALUE_TO_UPDATE_GPS_STATUS = 3000;
    public static float MIN_DISTANCE_TO_UPDATE_GPS_STATUS = 5f;
    public static String DEFAULT_APP_CURRENCY = "грн";


    public static final double DEFAULT_START_PRICE = 100;
    public static final double DEFAULT_PRICE_PER_KM = 5;
    public static final String DEFAULT_NAME_ROUTE = "ROUTE";
    public static final String ICON_TARIFFS_URI_PATH = "assets://tariff_icons/";
    public static final String DEFAULT_ICON_NAME = "default_tariff_icon.png";

    ///Parcelable and save state keys
    public static final String PARCELABLE_TARIFF_KEY = "PARCELABLE_TARIFF_KEY";

    public static final String SAVE_DATA_TARIFF_NAME_KEY = "SAVE_DATA_TARIFF_KEY";
    public static final String SAVE_DATA_TARIFF_PRICE_PER_KM_KEY = "SAVE_DATA_TARIFF_PRICE_PER_KM";
    public static final String SAVE_DATA_TARIFF_START_PRICE_KEY = "SAVE_DATA_TARIFF_START_PRICE_KEY";
    public static final String SAVE_DATA_TARIFF_TRANSPORT_ANIMAL_PRICE_KEY = "SAVE_DATA_TARIFF_TRANSPORT_ANIMAL_PRICE_KEY";
    public static final String SAVE_DATA_TARIFF_TRANSPORT_LUGGAGE_PRICE_KEY = "SAVE_DATA_TARIFF_TRANSPORT_LUGGAGE_PRICE_KEY";
    public static final String SAVE_DATA_TARIFF_SMOKE_SERVICE_PRICE_KEY = "SAVE_DATA_TARIFF_SMOKE_SERVICE_PRICE_KEY";
    public static final String SAVE_DATA_TARIFF_CHILD_CHAIR_PRICE_KEY = "SAVE_DATA_TARIFF_CHILD_CHAIR_PRICE_KEY";
    public static final String SAVE_DATA_TARIFF_OTHERS_SERVICES_PRICE_KEY = "SAVE_DATA_TARIFF_OTHERS_SERVICES_PRICE_KEY";

   public static final String SAVE_DATA_CURRENT_ROUTE_PAY_KEY = "SAVE_DATA_CURRENT_ROUTE_PAY_KEY";
    public static final String SAVE_DATA_CURRENT_TARIFF = "SAVE_DATA_CURRENT_TARIFF";
    public static final String SAVE_DATA_CURRENT_ROUTE = "SAVE_DATA_CURRENT_ROUTE";

    ///Shared pref keys

    public static final String SHARED_PREF_NAME_FILE = "selected_preference";


    public static final String IS_DEFAULT_TARIFF_AVAILABLE = "IS_DEFAULT_TARIFF_AVAILABLE";
    public static final String SHARED_PREF_DEFAULT_NAME_TARIFF ="SHARED_PREF_DEFAULT_NAME_TARIFF";
    public static final String SHARED_PREF_DEFAULT_TARIFF_START_PRICE = "SHARED_PREF_DEFAULT_TARIFF_START_PRICE";
    public static final String SHARED_PREF_DEFAULT_TARIFF_PRICE_PER_KM = "SHARED_PREF_DEFAULT_TARIFF_PRICE_PER_KM";
    public static final String SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_ANIMAL_PRICE = "SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_ANIMAL_PRICE";
    public static final String SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_LUGGAGE = "SHARED_PREF_DEFAULT_TARIFF_TRANSPORTATION_LUGGAGE";
    public static final String SHARED_PREF_DEFAULT_TARIFF_SMOKE_PRICE = "SHARED_PREF_DEFAULT_TARIFF_SMOKE_PRICE";
    public static final String SHARED_PREF_DEFAULT_TARIFF_CHILD_CHAIR_PRICE = "SHARED_PREF_DEFAULT_TARIFF_CHILD_CHAIR_PRICE";
    public static final String SHARED_PREF_DEFAULT_TARIFF_OTHERS_SERVICES_PRICE = "SHARED_PREF_DEFAULT_TARIFF_OTHERS_SERVICES_PRICE";

    ///prefixes
    public static final String PREFIX_PRICE ="price:";


/// Parcelable models keys
    public static final String MODEL_ROUTE_BASE_PARCELABLE_KEY = "MODEL_ROUTE_MODEL_PARCELABLE_KEY";
    public static final String MODEL_ROUTE_PARCELABLE_KEY = "MODEL_ROUTE_PARCELABLE_KEY";
    public static final String MODEL_TARIFF_PARCELABLE_KEY = "MODEL_TARIFF_PARCELABLE_KEY";
    public static final String MODEL_COORDINATES_PARCELABLE_KEY = "MODEL_COORDINATES_PARCELABLE_KEY";

}
