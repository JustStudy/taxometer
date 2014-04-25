package com.developer.taxometer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.R;
import com.developer.taxometer.models.Route;
import com.developer.taxometer.services.LocationService;
import com.developer.taxometer.utils.AppConstants;
import com.developer.taxometer.utils.LanguageHelper;
import com.developer.taxometer.utils.TimerUtils;

/**
 * Created by developer on 11.04.14.
 */
public class ResultRouteActivity extends SherlockActivity {

    private Route currentRoute;
    private double additionalServicePrice;
    private double payPerKmDistance;
    private double startPrice;

    ///View controls
    TextView textViewTripDistance;
    TextView textViewTripTime;
    TextView textViewPriceForAdditionalServices;
    TextView textViewPricePerKm;
    TextView textViewTotalPrice;
    TextView textViewWaitingTime;
    TextView textViewStartPrice;

    Button buttonHome;
    Button buttonToRouteScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);

        super.onCreate(savedInstanceState);
        currentRoute = getIntent().getExtras().getParcelable(AppConstants.MODEL_ROUTE_PARCELABLE_KEY);
        setContentView(R.layout.result_layout);
        initControls();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateAdditionPrice();

        textViewTripDistance.setText(getResources().getString(R.string.text_result_length_of_trip) + " " + String.format("%.0f", currentRoute.getRouteDistance() * 1000)+"  "+getString(R.string.text_route_layout_meters));
        String time = TimerUtils.millisecondsTimeToString(currentRoute.getRouteTime());
        textViewTripTime.setText(getResources().getString(R.string.text_result_trip_time) + " " + time);
        textViewPriceForAdditionalServices.setText(getResources().getString(R.string.text_result_price_for_additional_services) + " " + String.format("%.2f", additionalServicePrice) + " " + LocationService.locationService.getTaxiCurrency());
        textViewPricePerKm.setText(getResources().getString(R.string.text_result_price_per_km) + " " + String.format("%.2f", payPerKmDistance) + " " + LocationService.locationService.getTaxiCurrency());
        textViewTotalPrice.setText(getResources().getString(R.string.text_result_total_price) + " " + String.format("%.2f", currentRoute.getTotalPrice()) + " " + LocationService.locationService.getTaxiCurrency());
        String waitingTime = TimerUtils.millisecondsTimeToString(currentRoute.getWaitingTime());
        textViewWaitingTime.setText(getString(R.string.text_result_waiting_time) + " " + waitingTime);
        textViewStartPrice.setText(getString(R.string.text_result_start_price) + " " + String.format("%.2f", currentRoute.getTariffModel().getStartPayMoney()) + " " + LocationService.locationService.getTaxiCurrency());


    }

    @Override
    public void onBackPressed() {

    }

    private void initControls() {

        textViewPriceForAdditionalServices = (TextView) findViewById(R.id.textViewResultLayoutAdditionPrice);
        textViewTripTime = (TextView) findViewById(R.id.textViewResultLayoutTripTime);
        textViewPricePerKm = (TextView) findViewById(R.id.textViewResultLayoutPricePerKm);
        textViewTripDistance = (TextView) findViewById(R.id.textViewResultLayoutTripDistance);
        textViewTotalPrice = (TextView) findViewById(R.id.textViewResultLayoutTotalPrice);
        textViewWaitingTime = (TextView) findViewById(R.id.textViewResultLayoutWaitingTime);
        textViewStartPrice = (TextView) findViewById(R.id.textViewResultLayoutStartPrice);

        //buttons
        buttonHome = (Button) findViewById(R.id.buttonResultLayoutToHome);
        buttonToRouteScreen = (Button) findViewById(R.id.buttonResultLayoutToRoute);

        buttonHome.setOnClickListener(buttonToHomeClickListener);
        buttonToRouteScreen.setOnClickListener(buttonToRouteScreenClickListener);

    }


    private void calculateAdditionPrice() {

        if (currentRoute.getTariffModel().isAnimalIsTransport()) {
            additionalServicePrice = additionalServicePrice + currentRoute.getTariffModel().getAnimalTransportationPrice();
        }
        if (currentRoute.getTariffModel().isLuggageIsTransport()) {
            additionalServicePrice = additionalServicePrice + currentRoute.getTariffModel().getLuggageTransportationPrice();
        }
        if (currentRoute.getTariffModel().isSmokeService()) {
            additionalServicePrice = additionalServicePrice + currentRoute.getTariffModel().getSmokeServicePrice();
        }
        if (currentRoute.getTariffModel().isChildService()) {
            additionalServicePrice = additionalServicePrice + currentRoute.getTariffModel().getChildChairServicePrice();
        }
        if (currentRoute.getTariffModel().isOtherService()) {
            additionalServicePrice = additionalServicePrice + currentRoute.getTariffModel().getOtherServicePrice();
        }
        double bufPrice = currentRoute.getTotalPrice() - additionalServicePrice;
        payPerKmDistance = bufPrice - currentRoute.getTariffModel().getStartPayMoney();
    }

    View.OnClickListener buttonToHomeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            finish();
            startActivity(new Intent(ResultRouteActivity.this, MainStartActivity.class));
        }
    };


    View.OnClickListener buttonToRouteScreenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Route currentRoute = LocationService.locationService.getCurrentRoute();
            finish();
            startActivity(new Intent(ResultRouteActivity.this, StartRouteActivity.class));
        }
    };
}
