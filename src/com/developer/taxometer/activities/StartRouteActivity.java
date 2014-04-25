package com.developer.taxometer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.adapter.TariffListAdapter;
import com.developer.taxometer.database.DAORoute;
import com.developer.taxometer.database.DAOTariffModel;
import com.developer.taxometer.database.DataBaseHelperFactory;
import com.developer.taxometer.database.DataBaseOrmHelper;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.services.LocationService;
import com.developer.taxometer.utils.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StartRouteActivity extends SherlockActivity {

    private TextView textViewProfileTariffName;
    private TextView textViewCashToPay;
    private TextView textViewDistance;
    private TextView textViewWholeTime;
    private TextView textViewPricePerKm;
    private TextView textViewStartPay;
    private TextView textViewAnimalServicePrice;
    private TextView textViewLuggageServicePrice;
    private TextView textViewSmokeServicePrice;
    private TextView textViewChildrenChairServicePrice;
    private TextView textViewOtherServicePrice;
    private TextView textViewWaitingTime;


    private Button startRouteButton;
    private Button exitButton;
    private Button waitButton;


    private LocationManager locationManager;


    private DAORoute daoRoute;

    private float routeDistance;
    private double routeLongitude;
    private double routeLatitude;


    private double additionServicesValue;

    private double anotherDistance;
    private double thirdDistance;

    private ImageView animalServiceImage;
    private ImageView luggageServiceImage;
    private ImageView smokeServiceImage;
    private ImageView childChairImage;
    private ImageView othersServiceImage;

    private static boolean SHOW_DIALOG_WITH_TARIFFS = true;
    private static final String WAIT_TIMER_TIME_SAVE_KEY = "WAIT_TIMER_TIME_SAVE_KEY";
    private static final String WHOLE_TIME_SAVE_KEY = "WHOLE_TIME_SAVE_KEY";
    private boolean WHOLE_TIMER_IS_RUNNING = false;
    private boolean WAIT_TIMER_IS_RUNNING = false;

    private AlertDialog alertDialog;
    ///Location service data
    LocationService locationServiceInstanceThisActivity;
    private double secondBufferDistance;
    private double thirdBufferDistance;
    private double firstBufferDistance;

    Handler updateTextViewHandler = new Handler();

    Runnable updateTextViewRunnable = new Runnable() {
        @Override
        public void run() {

            updateTextViewMethod();
            updateTextViewHandler.postDelayed(this, locationServiceInstanceThisActivity.getGpsUpdateFrequency());

        }
    };

    private void updateTextViewMethod() {

        if (firstBufferDistance != LocationService.getThirdDistance()) {
            firstBufferDistance = LocationService.getThirdDistance();
            textViewDistance.setText(String.format("%.0f", firstBufferDistance * 1000) + " " + getString(R.string.text_route_layout_meters));
            double bufferTotalPrice = locationServiceInstanceThisActivity.getCurrentTariffModel().getMoneyPerKm() * firstBufferDistance + locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getStartPayMoney();
            locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(bufferTotalPrice);
            textViewCashToPay.setText(String.format("%.2f", bufferTotalPrice) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
            locationServiceInstanceThisActivity.getCurrentRoute().setRouteDistance(firstBufferDistance);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationServiceInstanceThisActivity = LocationService.locationService;

        if (locationServiceInstanceThisActivity == null) {
            startService(new Intent(StartRouteActivity.this, LocationService.class));
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.route_layout);
        initControls();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isGPSEnabled()) {

            showSettingsAlert();

        }

        if (locationServiceInstanceThisActivity.getCurrentTariffModel() != null) {
            refreshServiceButtons();
            setCurrentTariffValuesToViews();

        } else {

            locationServiceInstanceThisActivity.clearServiceRouteData();
                 if(TaximeterApplication.IS_SHOW_DIALOG_TARIFFS){
                     showAlertTariffDialog();
                 }
                 else{

                     SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(StartRouteActivity.this);
                     locationServiceInstanceThisActivity.setCurrentTariffModel(sharedPreferenceHelper.readDefaultTariffFromSharedPref());
                     Toast.makeText(StartRouteActivity.this, getString(R.string.alert_dialog_toast_text_chose_tariff) + " " + locationServiceInstanceThisActivity.getCurrentTariffModel().getNameTariff(), Toast.LENGTH_LONG).show();
                     setCurrentTariffValuesToViews();
                 }

        }


        if (locationServiceInstanceThisActivity.WHOLE_TIMER_IS_RUNNING) {
            locationServiceInstanceThisActivity.getTimerUtilsWholeTime().setTextView(getString(R.string.text_whole_time), textViewWholeTime);

            updateTextViewHandler.postDelayed(updateTextViewRunnable, locationServiceInstanceThisActivity.getGpsUpdateFrequency());
            waitButton.setClickable(true);

        } else {
            waitButton.setClickable(false);
        }
        if (locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING) {
            locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().setTextView(getString(R.string.text_wait_time), textViewWaitingTime);

        }

        textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());

        textViewWaitingTime.setText(getString(R.string.text_wait_time) + " " + TimerUtils.millisecondsTimeToString(locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().getFinalTime()));
        textViewWholeTime.setText(getString(R.string.text_whole_time) + " " + TimerUtils.millisecondsTimeToString(locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().getFinalTime()));

    }


    @Override
    protected void onDestroy() {
        updateTextViewHandler.removeCallbacks(updateTextViewRunnable);
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (TaximeterAlertDialog.alert != null) {
            TaximeterAlertDialog.alert.dismiss();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {


        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initControls() {

        textViewCashToPay = (TextView) findViewById(R.id.textViewCashValue);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        textViewProfileTariffName = (TextView) findViewById(R.id.textViewRouteCurrentTariffName);
        textViewWholeTime = (TextView) findViewById(R.id.textViewWholeTime);
        textViewPricePerKm = (TextView) findViewById(R.id.textViewPricePerKm);
        textViewStartPay = (TextView) findViewById(R.id.textViewStartPrice);
        textViewWaitingTime = (TextView) findViewById(R.id.textViewWaitingTime);

        textViewAnimalServicePrice = (TextView) findViewById(R.id.textViewAnimalPrice);
        textViewLuggageServicePrice = (TextView) findViewById(R.id.textViewLuggagePrice);
        textViewSmokeServicePrice = (TextView) findViewById(R.id.textViewSmokePrice);
        textViewChildrenChairServicePrice = (TextView) findViewById(R.id.textViewChildrenChairPrice);
        textViewOtherServicePrice = (TextView) findViewById(R.id.textViewOthersPrice);

        startRouteButton = (Button) findViewById(R.id.button_route_layout_start);
        waitButton = (Button) findViewById(R.id.button_route_layout_wait);
        exitButton = (Button) findViewById(R.id.buttonRouteLayoutExit);

        startRouteButton.setOnClickListener(buttonStartRouteClickListener);
        waitButton.setOnClickListener(waitButtonClickListener);
        exitButton.setOnClickListener(exitButtonListener);

        textViewDistance.setText(0 + " " + getString(R.string.text_route_layout_meters));
        if (LocationService.START_LOCATION_DATA) {

            startRouteButton.setText(getResources().getString(R.string.text_route_layout_route_finish));

        } else {
            startRouteButton.setText(getResources().getString(R.string.text_route_layout_route_start));
        }

        if (locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING) {
            waitButton.setText(getString(R.string.text_route_layout_route_continue));
        } else {
            waitButton.setText(getString(R.string.text_route_layout_route_wait));
        }


        exitButton.setText(getResources().getString(R.string.text_route_layout_route_exit));

        //init images

        luggageServiceImage = (ImageView) findViewById(R.id.imageViewLuggage);
        animalServiceImage = (ImageView) findViewById(R.id.imageViewAnimals);
        smokeServiceImage = (ImageView) findViewById(R.id.imageViewSmoke);
        childChairImage = (ImageView) findViewById(R.id.imageViewChildrenChair);
        othersServiceImage = (ImageView) findViewById(R.id.imageViewOthers);

        luggageServiceImage.setOnClickListener(luggageServiceButtonListener);
        animalServiceImage.setOnClickListener(animalServiceButtonListener);
        smokeServiceImage.setOnClickListener(smokeServiceClickListener);
        childChairImage.setOnClickListener(childrenChairClickListener);
        othersServiceImage.setOnClickListener(othersServiceClickListener);


    }



    private void showAlertTariffDialog() {


        TariffListAdapter tariffListAdapter;
        getListTariffsFromDB(locationServiceInstanceThisActivity.arrayListTariffs);

        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartRouteActivity.this);

        alertDialogBuilder.setCancelable(false);
        View viewDialog;
        if (locationServiceInstanceThisActivity.arrayListTariffs.size() > 0) {

            alertDialogBuilder.setTitle(getString(R.string.alert_dialog_text_title_choose_tariff));
            viewDialog = inflater.inflate(R.layout.dialog_layout_show_list_tariffs, null);
            Button dialogButtonUseDefaultTariff = (Button) viewDialog.findViewById(R.id.buttonDialogUseDefaultTariff);
            final CheckBox dialogCheckBox = (CheckBox) viewDialog.findViewById(R.id.checkBoxDialogDontShowDialog);
            ListView dialogListView = (ListView) viewDialog.findViewById(R.id.listViewDialogTariffs);
            tariffListAdapter = new TariffListAdapter(this, R.layout.one_tariff_item, locationServiceInstanceThisActivity.arrayListTariffs);
            dialogListView.setAdapter(tariffListAdapter);
            alertDialogBuilder.setView(viewDialog);
            alertDialog = alertDialogBuilder.create();
            dialogButtonUseDefaultTariff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(StartRouteActivity.this);
                    locationServiceInstanceThisActivity.setCurrentTariffModel(sharedPreferenceHelper.readDefaultTariffFromSharedPref());

                    //set tariff name to view
                    if (locationServiceInstanceThisActivity.getCurrentTariffModel() == null) {
                        Toast.makeText(StartRouteActivity.this, getString(R.string.alert_dialog_text_no_default_tariff), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(StartRouteActivity.this, getString(R.string.alert_dialog_toast_text_chose_tariff) + " " + locationServiceInstanceThisActivity.getCurrentTariffModel().getNameTariff(), Toast.LENGTH_LONG).show();
                        setCurrentTariffValuesToViews();
                        alertDialog.cancel();
                        controlStateDialog(dialogCheckBox);

                    }

                }
            });

            dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    locationServiceInstanceThisActivity.setCurrentTariffModel(locationServiceInstanceThisActivity.arrayListTariffs.get(position));

                    //set tariff name to view
                    setCurrentTariffValuesToViews();
                    alertDialog.cancel();
                    Toast.makeText(StartRouteActivity.this, getString(R.string.alert_dialog_toast_text_chose_tariff) + " " + locationServiceInstanceThisActivity.getCurrentTariffModel().getNameTariff(), Toast.LENGTH_LONG).show();
                   controlStateDialog(dialogCheckBox);

                }
            });
            // show it

        } else {
            alertDialogBuilder.setTitle(getString(R.string.alert_dialog_text_no_tariffs));
            /// viewDialog = inflater.inflate(R.layout.alert_dialog_no_tariffs, null);
            alertDialogBuilder.setNegativeButton(getString(R.string.alert_dialog_text_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(StartRouteActivity.this, MainStartActivity.class));
                }
            });
            alertDialogBuilder.setPositiveButton(getString(R.string.alert_dialog_text_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(StartRouteActivity.this, CreateNewTariffActivity.class));
                }
            });

            alertDialog = alertDialogBuilder.create();

        }

        alertDialog.show();
    }

    private void getListTariffsFromDB(ArrayList<TariffModel> arrayListTariffs) {

        DAOTariffModel daoTariffModel;

        try {

            daoTariffModel = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoTariffModel();
            List<TariffModel> tariffModelList = daoTariffModel.getAllTariffModels();
            if (tariffModelList.size() > 0) {

                arrayListTariffs.addAll(tariffModelList);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private boolean isGPSEnabled() {
        locationManager = (LocationManager) TaximeterApplication.appContext.getSystemService(LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getString(R.string.text_gps_dialog_title));

        alertDialog.setMessage(getString(R.string.text_gps_dialog_message));

        alertDialog.setPositiveButton(getString(R.string.text_gps_text_setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton(getString(R.string.text_gps_text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    //listeners OnClick
    View.OnClickListener buttonStartRouteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!isGPSEnabled()) {

                showSettingsAlert();

            } else {

                if (!LocationService.START_LOCATION_DATA) {

                    updateTextViewHandler.postDelayed(updateTextViewRunnable, locationServiceInstanceThisActivity.getGpsUpdateFrequency());
                    ////timer data

                    locationServiceInstanceThisActivity.getTimerUtilsWholeTime().setTextView(getString(R.string.text_whole_time), textViewWholeTime);
                    locationServiceInstanceThisActivity.getTimerUtilsWholeTime().startTimer();
                    locationServiceInstanceThisActivity.WHOLE_TIMER_IS_RUNNING = true;
                    locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getStartPayMoney());
                    LocationService.START_LOCATION_DATA = true;

                    //    startService(new Intent(StartRouteActivity.this, LocationService.class));

                    startRouteButton.setText(getResources().getString(R.string.text_route_layout_route_finish));
                    locationServiceInstanceThisActivity.getCurrentRoute().setTariffModel(locationServiceInstanceThisActivity.getCurrentTariffModel());
                    locationServiceInstanceThisActivity.getCurrentRoute().setRouteDistance(0);
                    textViewCashToPay.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
                    waitButton.setClickable(true);
                    Toast.makeText(StartRouteActivity.this, "Routing started", Toast.LENGTH_SHORT).show();

                     DataBaseHelperFactory.getDataBaseOrmHelper().setRouteInstanceToDataBase(DataBaseOrmHelper.CREATE_DATA_MODEL_IN_DATA_BASE, locationServiceInstanceThisActivity.getCurrentRoute());

                } else {

                    updateTextViewHandler.removeCallbacks(updateTextViewRunnable);

                    locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().stopTimer();
                    locationServiceInstanceThisActivity.getTimerUtilsWholeTime().stopTimer();
                    locationServiceInstanceThisActivity.WHOLE_TIMER_IS_RUNNING = false;
                    locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING = false;

                    locationServiceInstanceThisActivity.getCurrentRoute().setWaitingTime(locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().getFinalTime());
                    locationServiceInstanceThisActivity.getCurrentRoute().setRouteTime(locationServiceInstanceThisActivity.getTimerUtilsWholeTime().getFinalTime());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.MODEL_ROUTE_PARCELABLE_KEY, locationServiceInstanceThisActivity.getCurrentRoute());

                    LocationService.START_LOCATION_DATA = false;
                    // stopService(new Intent(StartRouteActivity.this, LocationService.class));

                    DataBaseHelperFactory.getDataBaseOrmHelper().setRouteInstanceToDataBase(DataBaseOrmHelper.REFRESH_DATA_MODEL_IN_DATA_BASE, locationServiceInstanceThisActivity.getCurrentRoute());
                    startActivity(new Intent(StartRouteActivity.this, ResultRouteActivity.class).putExtras(bundle));
                    finish();
                    if (LocationService.locationService != null) {

                        LocationService.locationService.clearServiceRouteData();
                    }
                }

            }
        }
    };


    View.OnClickListener waitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (!locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING) {

                waitButton.setText(getString(R.string.text_route_layout_route_continue));
                locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().setTextView(getString(R.string.text_wait_time), textViewWaitingTime);
                locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().startTimer();
                locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING = true;

            } else {

                waitButton.setText(getString(R.string.text_route_layout_route_wait));
                locationServiceInstanceThisActivity.getTimerUtilsWaitingTime().pauseTimer();
                locationServiceInstanceThisActivity.WAIT_TIMER_IS_RUNNING = false;
            }

        }
    };

    View.OnClickListener exitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TaximeterAlertDialog.showExitAlertDialog(StartRouteActivity.this, MainStartActivity.class);

        }
    };


    View.OnClickListener animalServiceButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (locationServiceInstanceThisActivity.getCurrentTariffModel().isAnimalIsTransport()) {

                locationServiceInstanceThisActivity.getCurrentTariffModel().setAnimalIsTransport(false);
                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() - locationServiceInstanceThisActivity.getCurrentTariffModel().getAnimalTransportationPrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() - locationServiceInstanceThisActivity.getCurrentTariffModel().getAnimalTransportationPrice());
                animalServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_normal_selector));

            } else {

                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getAnimalTransportationPrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() + locationServiceInstanceThisActivity.getCurrentTariffModel().getAnimalTransportationPrice());
                animalServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setAnimalIsTransport(true);


            }
            textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        }
    };

    View.OnClickListener luggageServiceButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (locationServiceInstanceThisActivity.getCurrentTariffModel().isLuggageIsTransport()) {

                locationServiceInstanceThisActivity.getCurrentTariffModel().setLuggageIsTransport(false);
                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() - locationServiceInstanceThisActivity.getCurrentTariffModel().getLuggageTransportationPrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() - locationServiceInstanceThisActivity.getCurrentTariffModel().getLuggageTransportationPrice());
                luggageServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_normal_selector));


            } else {

                locationServiceInstanceThisActivity.getCurrentTariffModel().setLuggageIsTransport(true);
                luggageServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() + locationServiceInstanceThisActivity.getCurrentTariffModel().getLuggageTransportationPrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getLuggageTransportationPrice());

            }
            textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        }
    };

    View.OnClickListener smokeServiceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (locationServiceInstanceThisActivity.getCurrentTariffModel().isSmokeService()) {

                smokeServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_normal_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setSmokeService(false);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() - locationServiceInstanceThisActivity.getCurrentTariffModel().getSmokeServicePrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() - locationServiceInstanceThisActivity.getCurrentTariffModel().getSmokeServicePrice());


            } else {

                smokeServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setSmokeService(true);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() + locationServiceInstanceThisActivity.getCurrentTariffModel().getSmokeServicePrice());
                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getSmokeServicePrice());

            }
            textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        }
    };
    View.OnClickListener childrenChairClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (locationServiceInstanceThisActivity.getCurrentTariffModel().isChildService()) {

                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() - locationServiceInstanceThisActivity.getCurrentTariffModel().getChildChairServicePrice());
                childChairImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_normal_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setChildService(false);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() - locationServiceInstanceThisActivity.getCurrentTariffModel().getChildChairServicePrice());
            } else {

                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getChildChairServicePrice());
                childChairImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setChildService(true);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() + locationServiceInstanceThisActivity.getCurrentTariffModel().getChildChairServicePrice());
            }
            textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        }
    };

    View.OnClickListener othersServiceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (locationServiceInstanceThisActivity.getCurrentTariffModel().isOtherService()) {

                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() - locationServiceInstanceThisActivity.getCurrentTariffModel().getOtherServicePrice());
                othersServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_normal_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setOtherService(false);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() - locationServiceInstanceThisActivity.getCurrentTariffModel().getOtherServicePrice());

            } else {

                locationServiceInstanceThisActivity.getCurrentRoute().setCashForAdditionServices(locationServiceInstanceThisActivity.getCurrentRoute().getCashForAdditionServices() + locationServiceInstanceThisActivity.getCurrentTariffModel().getOtherServicePrice());
                othersServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
                locationServiceInstanceThisActivity.getCurrentTariffModel().setOtherService(true);
                locationServiceInstanceThisActivity.getCurrentRoute().setTotalPrice(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice() + locationServiceInstanceThisActivity.getCurrentTariffModel().getOtherServicePrice());
            }
            textViewCashToPay.setText(String.valueOf(locationServiceInstanceThisActivity.getCurrentRoute().getTotalPrice()) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        }
    };


    @Override
    public void onBackPressed() {

        TaximeterAlertDialog.showExitAlertDialog(StartRouteActivity.this);

    }


    private void setCurrentTariffValuesToViews() {

        textViewProfileTariffName.setText(locationServiceInstanceThisActivity.getCurrentTariffModel().getNameTariff());
        textViewStartPay.setText(String.valueOf(getResources().getString(R.string.text_route_start_price) + String.format("%.2f", locationServiceInstanceThisActivity.getCurrentTariffModel().getStartPayMoney()) ) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());
        textViewPricePerKm.setText(String.valueOf(getResources().getString(R.string.text_route_price_per_km) + String.format("%.2f", locationServiceInstanceThisActivity.getCurrentTariffModel().getMoneyPerKm()) ) + " " + locationServiceInstanceThisActivity.getTaxiCurrency());

        textViewAnimalServicePrice.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentTariffModel().getAnimalTransportationPrice()) );
        textViewLuggageServicePrice.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentTariffModel().getLuggageTransportationPrice()) );
        textViewSmokeServicePrice.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentTariffModel().getSmokeServicePrice()) );
        textViewChildrenChairServicePrice.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentTariffModel().getChildChairServicePrice()) );
        textViewOtherServicePrice.setText(String.format("%.2f",locationServiceInstanceThisActivity.getCurrentTariffModel().getOtherServicePrice()));
    }

    private void refreshServiceButtons() {

        if (locationServiceInstanceThisActivity.getCurrentTariffModel().isAnimalIsTransport()) {
            animalServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
        }
        if (locationServiceInstanceThisActivity.getCurrentTariffModel().isLuggageIsTransport()) {
            luggageServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
        }
        if (locationServiceInstanceThisActivity.getCurrentTariffModel().isSmokeService()) {
            smokeServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
        }
        if (locationServiceInstanceThisActivity.getCurrentTariffModel().isChildService()) {
            childChairImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
        }
        if (locationServiceInstanceThisActivity.getCurrentTariffModel().isOtherService()) {
            othersServiceImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_service_pressed_selector));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (locationServiceInstanceThisActivity != null) {

                locationServiceInstanceThisActivity.clearServiceRouteData();
            } else {

                LocationService.locationService.clearServiceRouteData();
            }

            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void controlStateDialog(CheckBox checkBox){

        if(checkBox.isChecked()){

            TaximeterApplication.IS_SHOW_DIALOG_TARIFFS =false;
        } else {
            TaximeterApplication.IS_SHOW_DIALOG_TARIFFS =true;
        }
    }


}
