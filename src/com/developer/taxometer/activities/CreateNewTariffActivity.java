package com.developer.taxometer.activities;

import android.os.Bundle;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.utils.AppConstants;

/**
 * Created by developer on 28.03.14.
 */
public class CreateNewTariffActivity extends ManipulateTariffActivity {


    private void createDefaultTariff() {
        tariffModel = new TariffModel();
        tariffModel.setStartPayMoney(50);
        tariffModel.setNameTariff("default route");
        tariffModel.setMoneyPerKm(120.4);
        tariffModel.setId(10029);
        tariffModel.setTariffIcon(AppConstants.DEFAULT_ICON_NAME);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
     /*   if (savedInstanceState == null) {
            createDefaultTariff();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    @Override
    public void showSettingsAlert() {
        super.showSettingsAlert();
    }
}
