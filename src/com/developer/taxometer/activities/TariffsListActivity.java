package com.developer.taxometer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.developer.taxometer.R;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.adapter.TariffListAdapter;
import com.developer.taxometer.database.DAOTariffModel;
import com.developer.taxometer.database.DataBaseHelperFactory;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.utils.AppConstants;
import com.developer.taxometer.utils.LanguageHelper;
import com.developer.taxometer.utils.SharedPreferenceHelper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 26.03.14.
 */
public class TariffsListActivity extends SherlockActivity {

    private ListView tariffListView;
    private TariffListAdapter tariffListAdapter;
    private ArrayList<TariffModel> arrayListTariffs;


    private void initControls() {
        tariffListView = (ListView) findViewById(R.id.listViewListTariffs);
    }

    private void getListTariffsFromResources() {
        //  createTestDefaultTariff();

        getListTariffsFromDB();


    }


    private void getListTariffsFromDB() {

        DAOTariffModel daoTariffModel;
        TariffModel tariffModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////for language
        LanguageHelper.loadLocale(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariff_list_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initControls();

    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayListTariffs = new ArrayList<TariffModel>();
        getListTariffsFromResources();

        tariffListAdapter = new TariffListAdapter(this, R.layout.one_tariff_item, arrayListTariffs);
        tariffListView.setAdapter(tariffListAdapter);

        tariffListView.setOnItemClickListener(tariffListOnItemListener);


        registerForContextMenu(tariffListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           android.view.MenuInflater inflater = (android.view.MenuInflater) getMenuInflater();
        inflater.inflate(R.menu.tariff_edit_context_menu, (android.view.Menu) menu);
    }



    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        switch (item.getItemId()){
            case R.id.tariff_edit_context_menu_add:
                startActivity(new Intent(TariffsListActivity.this, CreateNewTariffActivity.class));
                break;

            case R.id.tariff_edit_context_menu_edit:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.PARCELABLE_TARIFF_KEY, arrayListTariffs.get(info.position));
                Intent intent = new Intent(TariffsListActivity.this, EditSomeTariffActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case  R.id.tariff_edit_context_menu_set_as_default:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                setTariffAsDefault(info.position);
                break;
            case R.id.tariff_edit_context_menu_delete:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                DAOTariffModel daoTariffModel;
                if(info.position ==tariffListAdapter.selectedPosition){
                    Toast.makeText(TariffsListActivity.this,"you cant remove default tariff",Toast.LENGTH_LONG).show();
                }  else {

                    try {


                        daoTariffModel = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoTariffModel();
                        daoTariffModel.delete(arrayListTariffs.get(info.position));
                        arrayListTariffs.remove(info.position);
                        arrayListTariffs.trimToSize();

                        if(info.position<tariffListAdapter.selectedPosition){
                            tariffListAdapter.selectedPosition--;
                        }
                        tariffListAdapter.notifyDataSetChanged();
                        tariffListAdapter.notifyDataSetInvalidated();
                        Toast.makeText(TaximeterApplication.appContext, "tariff removed successfully", Toast.LENGTH_SHORT).show();

                    } catch (SQLException e) {
                        Toast.makeText(TaximeterApplication.appContext, "Error....data did not removed", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

/*   @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.e("onContextItemSelected","selected");
        AdapterView.AdapterContextMenuInfo info;
        switch (item.getItemId()){
            case R.id.tariff_edit_context_menu_add:
                startActivity(new Intent(TariffsListActivity.this, CreateNewTariffActivity.class));
                break;

            case R.id.tariff_edit_context_menu_edit:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.PARCELABLE_TARIFF_KEY, arrayListTariffs.get(info.position));
                Intent intent = new Intent(TariffsListActivity.this, EditSomeTariffActivity.class);
                intent.putExtras(bundle);
               startActivity(intent);
                break;

            case  R.id.tariff_edit_context_menu_set_as_default:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                   setTariffAsDefault(info.position);
                 break;
            case R.id.tariff_edit_context_menu_delete:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                DAOTariffModel daoTariffModel;
                      if(info.position ==tariffListAdapter.selectedPosition){
                           Toast.makeText(TariffsListActivity.this,"you cant remove default tariff",Toast.LENGTH_LONG).show();
                      }  else {

                          try {


                              daoTariffModel = DataBaseHelperFactory.getDataBaseOrmHelper().getDaoTariffModel();
                              daoTariffModel.delete(arrayListTariffs.get(info.position));
                              arrayListTariffs.remove(info.position);
                              arrayListTariffs.trimToSize();

                              if(info.position<tariffListAdapter.selectedPosition){
                                  tariffListAdapter.selectedPosition--;
                              }
                              tariffListAdapter.notifyDataSetChanged();
                              tariffListAdapter.notifyDataSetInvalidated();
                              Toast.makeText(TaximeterApplication.appContext, "tariff removed successfully", Toast.LENGTH_SHORT).show();

                          } catch (SQLException e) {
                              Toast.makeText(TaximeterApplication.appContext, "Error....data did not removed", Toast.LENGTH_LONG).show();
                              e.printStackTrace();
                          }
                      }
                    break;
        }
        return super.onContextItemSelected((android.view.MenuItem) item);

    }*/

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(TariffsListActivity.this, CreateNewTariffActivity.class));
        return super.onOptionsItemSelected(item);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId()==R.id.item_add_new_tariff){
            startActivity(new Intent(TariffsListActivity.this, CreateNewTariffActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


     AdapterView.OnItemClickListener tariffListOnItemListener = new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               setTariffAsDefault(position);
         }
     };

    private void setTariffAsDefault(final int position){


        Log.e("onItemClick","clicked "+position);
      /*  RadioButton radioButton = ((RadioButton)view.findViewById(R.id.radioButtonTariffListViewItem));
        radioButton.setChecked(true);*/
        tariffListAdapter.selectedPosition = position;
        tariffListAdapter.notifyDataSetChanged();
        SharedPreferenceHelper.writeDefaultTariffToSharedPref(TariffsListActivity.this,arrayListTariffs.get(tariffListAdapter.selectedPosition));
        tariffListAdapter.notifyDataSetInvalidated();
        Log.e("wrote","element with position "+tariffListAdapter.selectedPosition);
    }
}
