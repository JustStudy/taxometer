package com.developer.taxometer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.database.DAOTariffModel;
import com.developer.taxometer.database.DataBaseHelperFactory;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.utils.AppConstants;
import com.developer.taxometer.utils.LanguageHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by developer on 28.03.14.
 */
public class ManipulateTariffActivity extends SherlockActivity {
    TariffModel tariffModel;

    protected TextView textViewNameRoute;
    protected TextView textViewPricePerKM;
    protected TextView textViewStartPrice;
    protected TextView textViewAnimalPrice;
    protected TextView textViewLuggagePrice;
    protected TextView textViewSmokePrice;
    protected TextView textViewChildrenChairPrice;
    protected TextView textViewOthersPrice;

    protected EditText editTextNameRoute;
    protected EditText editTextPricePerKm;
    protected EditText editTextStartPrice;
    protected EditText editTextAnimalPrice;
    protected EditText editTextLuggagePrice;
    protected EditText editTextSmokePrice;
    protected EditText editTextOthersPrice;
    protected EditText editTextChildrenChair;


    protected Button saveTrafficSetting;

    protected View mainRelativeRootView;

    protected String tariffNameCurrentValue = "Default Tariff Name";
    protected double pricePerKmCurrentValue;
    protected double startPriceCurrentValue;
    protected double transportAnimalCurrentValue;
    protected double transportLuggageCurrentValue;
    protected double smokeServiceCurrentValue;
    protected double childrenChairCurrentValue;
    protected double othersServicesPriceCurrentValue;


    private void initComponent() {

        mainRelativeRootView = this.getWindow().getDecorView().findViewById(R.id.rootRelativeEditTariff);
        View mainRootView = mainRelativeRootView.findViewById(R.id.scrollViewEditSomeTariff);
        View rootLinear = mainRootView.findViewById(R.id.rootLinearInScrollViewEditTariffLayout);

        //init first include
        View firstNameRouteInclude = rootLinear.findViewById(R.id.includeNameRouteSection);
        textViewNameRoute = (TextView) firstNameRouteInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextNameRoute = (EditText) firstNameRouteInclude.findViewById(R.id.editTextIncludeEditTariff);

        //init second include
        View secondPricePerKmInclude = rootLinear.findViewById(R.id.includePricePerKMSection);
        textViewPricePerKM = (TextView) secondPricePerKmInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextPricePerKm = (EditText) secondPricePerKmInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextPricePerKm.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init third include
        View thirdStartPriceInclude = rootLinear.findViewById(R.id.includeStartPriceSection);
        textViewStartPrice = (TextView) thirdStartPriceInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextStartPrice = (EditText) thirdStartPriceInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextStartPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init forth include
        View forthAnimalInclude = rootLinear.findViewById(R.id.includeAnimalSection);
        textViewAnimalPrice = (TextView) forthAnimalInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextAnimalPrice = (EditText) forthAnimalInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextAnimalPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init fifth include
        View fifthLuggageInclude = rootLinear.findViewById(R.id.includeLuggageSection);
        textViewLuggagePrice = (TextView) fifthLuggageInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextLuggagePrice = (EditText) fifthLuggageInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextLuggagePrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init childrenChair include
        View childrenChairInclude = rootLinear.findViewById(R.id.includeChildrenChairSection);
        textViewChildrenChairPrice = (TextView) childrenChairInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextChildrenChair = (EditText) childrenChairInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextChildrenChair.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init smoke include
        View smokeInclude = rootLinear.findViewById(R.id.includeSmokeSection);
        textViewSmokePrice =(TextView) smokeInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextSmokePrice = (EditText) smokeInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextSmokePrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        //init others include
        View othersInclude = rootLinear.findViewById(R.id.includeOthersSection);
        textViewOthersPrice = (TextView) othersInclude.findViewById(R.id.textViewHeaderIncludeEditTariff);
        editTextOthersPrice = (EditText) othersInclude.findViewById(R.id.editTextIncludeEditTariff);
        editTextOthersPrice.setInputType(InputType.TYPE_CLASS_NUMBER);


        //loadDefTrafficSetting = (Button) findViewById(R.id.buttonLoadData);
        saveTrafficSetting = (Button) findViewById(R.id.buttonSaveData);
        // loadTrafficSettingFromDB = (Button) findViewById(R.id.buttonLoadFromDB);


        // loadDefTrafficSetting.setOnClickListener(loadDefSettingListener);
        saveTrafficSetting.setOnClickListener(saveTrafficSettingsToDBListener);
        //loadTrafficSettingFromDB.setOnClickListener(loadTrafficSettingFromDBListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTextViewsData() {
        textViewNameRoute.setText(getResources().getString(R.string.text_route_name));
        textViewStartPrice.setText(getResources().getString(R.string.text_route_start_price));
        textViewPricePerKM.setText(getResources().getString(R.string.text_route_price_per_km));
        textViewAnimalPrice.setText(getResources().getString(R.string.text_route_animal_price));
        textViewLuggagePrice.setText(getResources().getString(R.string.text_route_luggage_price));
        textViewSmokePrice.setText(getResources().getString(R.string.text_route_smoke_price));
        textViewChildrenChairPrice.setText(getResources().getString(R.string.text_route_children_chair_price));
        textViewOthersPrice.setText(getResources().getString(R.string.text_route_others_price));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);

        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.edit_tariff_layout);
        setContentView(R.layout.edit_tariff_layout);
        getSupportActionBar().setHomeButtonEnabled(true);

        initComponent();
        setTextViewsData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataToEditText();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(AppConstants.SAVE_DATA_TARIFF_NAME_KEY, editTextNameRoute.getText().toString());
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_PRICE_PER_KM_KEY, Double.valueOf(editTextPricePerKm.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_START_PRICE_KEY, Double.valueOf(editTextStartPrice.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_TRANSPORT_ANIMAL_PRICE_KEY, Double.valueOf(editTextAnimalPrice.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_TRANSPORT_LUGGAGE_PRICE_KEY, Double.valueOf(editTextLuggagePrice.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_SMOKE_SERVICE_PRICE_KEY,Double.valueOf(editTextSmokePrice.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_CHILD_CHAIR_PRICE_KEY,Double.valueOf(editTextChildrenChair.getText().toString()));
        outState.putDouble(AppConstants.SAVE_DATA_TARIFF_OTHERS_SERVICES_PRICE_KEY,Double.valueOf(editTextOthersPrice.getText().toString()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        tariffNameCurrentValue = savedInstanceState.getString(AppConstants.SAVE_DATA_TARIFF_NAME_KEY);
        startPriceCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_START_PRICE_KEY);
        pricePerKmCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_PRICE_PER_KM_KEY);
        transportLuggageCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_TRANSPORT_LUGGAGE_PRICE_KEY);
        transportAnimalCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_TRANSPORT_ANIMAL_PRICE_KEY);
        smokeServiceCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_SMOKE_SERVICE_PRICE_KEY);
        childrenChairCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_CHILD_CHAIR_PRICE_KEY);
        othersServicesPriceCurrentValue = savedInstanceState.getDouble(AppConstants.SAVE_DATA_TARIFF_OTHERS_SERVICES_PRICE_KEY);

        super.onRestoreInstanceState(savedInstanceState);
    }


    private void setDataToEditText() {

        editTextNameRoute.setText(tariffNameCurrentValue);
        editTextPricePerKm.setText(String.valueOf(pricePerKmCurrentValue));
        editTextStartPrice.setText(String.valueOf(startPriceCurrentValue));
        editTextAnimalPrice.setText(String.valueOf(transportAnimalCurrentValue));
        editTextLuggagePrice.setText(String.valueOf(transportLuggageCurrentValue));
        editTextSmokePrice.setText(String.valueOf(smokeServiceCurrentValue));
        editTextChildrenChair.setText(String.valueOf(childrenChairCurrentValue));
        editTextOthersPrice.setText(String.valueOf(othersServicesPriceCurrentValue));
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

    // button listeners


    View.OnClickListener saveTrafficSettingsToDBListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (editTextNameRoute.getText().toString().isEmpty()) {

                Toast.makeText(TaximeterApplication.appContext, "Tariff name is empty. Please, input tariff name", Toast.LENGTH_LONG).show();

            } else {


                DAOTariffModel daotariffModel;
                try {

                    daotariffModel = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoTariffModel();
                    List<TariffModel> tariffModelList = daotariffModel.getAllTariffModels();
                    TariffModel tariffModel = new TariffModel();

                    tariffModel.setMoneyPerKm(Double.valueOf(editTextPricePerKm.getText().toString()));
                    tariffModel.setNameTariff(editTextNameRoute.getText().toString());
                    tariffModel.setStartPayMoney(Double.valueOf(editTextStartPrice.getText().toString()));
                    tariffModel.setAnimalTransportationPrice(Double.valueOf(editTextAnimalPrice.getText().toString()));
                    tariffModel.setLuggageTransportationPrice(Double.valueOf(editTextLuggagePrice.getText().toString()));
                    tariffModel.setSmokeServicePrice(Double.valueOf(editTextSmokePrice.getText().toString()));
                    tariffModel.setChildChairServicePrice(Double.valueOf(editTextChildrenChair.getText().toString()));
                    tariffModel.setOtherServicePrice(Double.valueOf(editTextOthersPrice.getText().toString()));

                    if (tariffModelList.size() > 0) {
                        boolean isFound = false;

                        for (int i = 0; i < tariffModelList.size(); i++) {
                            if (tariffModel.getNameTariff().equals(tariffModelList.get(i).getNameTariff())) {
                                isFound = true;
                                tariffModel.setId(tariffModelList.get(i).getId());
                                daotariffModel.update(tariffModel);
                                Toast.makeText(TaximeterApplication.appContext, "data was override successfully", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if (!isFound) {
                            daotariffModel.create(tariffModel);
                            Toast.makeText(TaximeterApplication.appContext, "data saved successfully", Toast.LENGTH_SHORT).show();
                        } else {

                        }

                    } else {
                        daotariffModel.create(tariffModel);
                        Toast.makeText(TaximeterApplication.appContext, "data saved successfully", Toast.LENGTH_SHORT).show();

                    }
                    NavUtils.navigateUpFromSameTask(ManipulateTariffActivity.this);

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }


        }
    };

}
