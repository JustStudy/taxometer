package com.developer.taxometer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.utils.AppConstants;

/**
 * Created by developer on 27.03.14.
 */
public class EditSomeTariffActivity extends ManipulateTariffActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {

            getParcelTariff();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getParcelTariff() {
        tariffModel = this.getIntent().getExtras().getParcelable(AppConstants.PARCELABLE_TARIFF_KEY);

        tariffNameCurrentValue = tariffModel.getNameTariff();
        startPriceCurrentValue = tariffModel.getStartPayMoney();
        transportAnimalCurrentValue = tariffModel.getAnimalTransportationPrice();
        transportLuggageCurrentValue = tariffModel.getLuggageTransportationPrice();
        pricePerKmCurrentValue = tariffModel.getMoneyPerKm();
        smokeServiceCurrentValue = tariffModel.getSmokeServicePrice();
        childrenChairCurrentValue = tariffModel.getChildChairServicePrice();
        othersServicesPriceCurrentValue = tariffModel.getOtherServicePrice();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

///dialogs

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Attention");

        alertDialog.setMessage("Tariff with same name was  Do you want to go to settings menu?");

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


}
