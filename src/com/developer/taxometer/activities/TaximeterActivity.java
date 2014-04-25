package com.developer.taxometer.activities;

import android.app.AlertDialog;
import android.content.*;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.services.LocationService;
import com.developer.taxometer.utils.Filters;
import com.developer.taxometer.utils.LanguageHelper;

/**
 * Created by developer on 24.03.14.
 */
public class TaximeterActivity extends SherlockActivity {

    private Button startLocationServiceButton;

    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private TextView textViewDistance;

    private BroadCastListener broadCastListener;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.taximeter_layout);
        initControls();


        if (!isGPSEnabled()) {
            showSettingsAlert();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
        Log.e("onResume()", "in this method");
        if (LocationService.locationService == null) {
            startLocationServiceButton.setText(getResources().getString(R.string.text_start_service));
        } else {
            startLocationServiceButton.setText(getResources().getString(R.string.text_stop_service));
        }

        textViewDistance.setText(getResources().getString(R.string.text_distance) + " " + distance);
        textViewLongitude.setText(getResources().getString(R.string.text_longitude) + " " + longitude);
        textViewLatitude.setText(getResources().getString(R.string.text_latitude) + " " + latitude);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  unregisterReceiver(broadCastListener);
        Log.e("onStop()", "in this method");

    }

    private void initControls() {
        startLocationServiceButton = (Button) findViewById(R.id.buttonStartLocationService);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);

        startLocationServiceButton.setOnClickListener(startLocationButtonListener);

    }

    View.OnClickListener startLocationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled()) {
                showSettingsAlert();

            } else {
                if (LocationService.locationService == null) {
                    startService(new Intent(TaximeterActivity.this, LocationService.class));
                    startLocationServiceButton.setText(getResources().getString(R.string.text_stop_service));
                    Toast.makeText(TaximeterActivity.this, "Service started", Toast.LENGTH_SHORT).show();
                } else {
                    startLocationServiceButton.setText(getResources().getString(R.string.text_start_service));
                    stopService(new Intent(TaximeterActivity.this, LocationService.class));
                    Toast.makeText(TaximeterActivity.this, "Service finished", Toast.LENGTH_SHORT).show();
                }

            }

        }

    };


    private void registerListener() {
        if (broadCastListener == null) {
            broadCastListener = new BroadCastListener();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Filters.BROADCAST_FILTER_REFRESH_COORD);
            registerReceiver(broadCastListener, filter);


        }
    }

    private class BroadCastListener extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Filters.BROADCAST_FILTER_REFRESH_COORD)) {
                Bundle bundle = new Bundle();
                bundle = intent.getExtras();

                latitude = bundle.getDouble(Filters.INTENT_FLAG_VALUE_LATITUDE);
                longitude = bundle.getDouble(Filters.INTENT_FLAG_VALUE_LONGITUDE);
                distance = bundle.getFloat(Filters.INTENT_FLAG_VALUE_DISTANCE);

                textViewLatitude.setText(getResources().getString(R.string.text_latitude) + " " + latitude);
                textViewLongitude.setText(getResources().getString(R.string.text_longitude) + " " + longitude);
                textViewDistance.setText(getResources().getString(R.string.text_distance) + " " + distance);
            }

        }
    }

    private boolean isGPSEnabled() {
        locationManager = (LocationManager) TaximeterApplication.appContext.getSystemService(LOCATION_SERVICE);


        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }


    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(Filters.INTENT_FLAG_VALUE_DISTANCE, distance);
        outState.putDouble(Filters.INTENT_FLAG_VALUE_LONGITUDE, longitude);
        outState.putDouble(Filters.INTENT_FLAG_VALUE_LATITUDE, latitude);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        latitude = savedInstanceState.getDouble(Filters.INTENT_FLAG_VALUE_LATITUDE);
        longitude = savedInstanceState.getDouble(Filters.INTENT_FLAG_VALUE_LONGITUDE);
        distance = savedInstanceState.getFloat(Filters.INTENT_FLAG_VALUE_DISTANCE);
    }
}
