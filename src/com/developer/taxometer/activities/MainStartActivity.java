package com.developer.taxometer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockActivity;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.services.LocationService;
import com.developer.taxometer.utils.LanguageHelper;

public class MainStartActivity extends SherlockActivity {

    private Button startTaximeterButton;
    private Button configurationsButton;
    private Button exitButton;
    private Button goToTariffsListButton;

    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initControls();

        if (!isGPSEnabled()) {

            showSettingsAlert();

        } else {

            startService(new Intent(MainStartActivity.this, LocationService.class));
        }

    }


    private void initControls() {

        startTaximeterButton = (Button) findViewById(R.id.buttonStartTaximeter);
        configurationsButton = (Button) findViewById(R.id.buttonConfigurations);
        goToTariffsListButton = (Button) findViewById(R.id.buttonGoToTariffsList);
        exitButton = (Button) findViewById(R.id.buttonExit);

        startTaximeterButton.setOnClickListener(startTaximeterListener);
        configurationsButton.setOnClickListener(configurationButtonListener);
        goToTariffsListButton.setOnClickListener(goToTariffsListButtonListener);
        exitButton.setOnClickListener(exitAppButtonListener);
    }


    ///Button Listeners
    View.OnClickListener startTaximeterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            startActivity(new Intent(MainStartActivity.this, StartRouteActivity.class));

        }
    };


    View.OnClickListener configurationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            startActivity(new Intent(MainStartActivity.this, ConfigurationActivity.class));
        }
    };

    View.OnClickListener exitAppButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showExitDialog();
        }
    };

    View.OnClickListener goToTariffsListButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            startActivity(new Intent(MainStartActivity.this, TariffsListActivity.class));
        }
    };


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

    private boolean isGPSEnabled() {
        locationManager = (LocationManager) TaximeterApplication.appContext.getSystemService(LOCATION_SERVICE);


        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }


    @Override
    public void onBackPressed() {

        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainStartActivity.this);
        alertDialog.setTitle(getString(R.string.alert_dialog_exit_dialog));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.alert_dialog_exit_question));

        alertDialog.setPositiveButton(getString(R.string.alert_dialog_text_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                stopService(new Intent(MainStartActivity.this,LocationService.class));
            }
        });

        alertDialog.setNegativeButton(R.string.alert_dialog_text_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
}
