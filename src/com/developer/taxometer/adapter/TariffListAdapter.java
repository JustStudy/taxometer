package com.developer.taxometer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.developer.taxometer.R;
import com.developer.taxometer.activities.EditSomeTariffActivity;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.utils.AppConstants;
import com.developer.taxometer.utils.SharedPreferenceHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by developer on 26.03.14.
 */
public class TariffListAdapter extends ArrayAdapter<TariffModel> {
    private ImageLoader imageLoader;
    private ArrayList<TariffModel> listTariffsNames;
    private Context context;
   public   int selectedPosition =-100;



    public TariffListAdapter(Context context, int resource, ArrayList<TariffModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.listTariffsNames = objects;


        //imageLoader configs
        this.imageLoader = ImageLoader.getInstance();

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY + 4)
                .threadPoolSize(3)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(displayImageOptions)
                .build();

        imageLoader.init(configuration);


        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(context);
        TariffModel controlTariffModel = sharedPreferenceHelper.readDefaultTariffFromSharedPref();

        if(controlTariffModel!=null){

            for (int i=0;i<listTariffsNames.size();i++){

                if(controlTariffModel.getNameTariff().equals(listTariffsNames.get(i).getNameTariff())){

                    selectedPosition = i;
                    break;
                }

            }
        } else {
            Toast.makeText(context,"Please,choose default tariff",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public int getCount() {
        return listTariffsNames.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.one_tariff_item, null);
            viewHolder = new ViewHolder();
            viewHolder.setTariffName((TextView) view.findViewById(R.id.textViewNameTariff));
            viewHolder.setTariffIcon((ImageView) view.findViewById(R.id.imageViewIconTariff));
            viewHolder.setTariffPricePerKm((TextView) view.findViewById(R.id.textViewTariffListViewItemPricePerKm));
            viewHolder.setTariffStartPrice((TextView) view.findViewById(R.id.textViewTariffListViewItemStartPrice));
            viewHolder.setTariffRadioButton((RadioButton)view.findViewById(R.id.radioButtonTariffListViewItem));
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();

        }

        /// manipulate radio button
        viewHolder.getTariffRadioButton().setChecked(position==selectedPosition);
        viewHolder.getTariffRadioButton().setTag(position);




        viewHolder.getTariffName().setText(listTariffsNames.get(position).getNameTariff());
        viewHolder.getTariffPricePerKm().setText(context.getResources().getString(R.string.text_route_price_per_km) + " " + String.valueOf(listTariffsNames.get(position).getMoneyPerKm()));
        viewHolder.getTariffStartPrice().setText(context.getResources().getString(R.string.text_route_start_price) + " " + String.valueOf(listTariffsNames.get(position).getStartPayMoney()));



        if(listTariffsNames.get(position).getTariffIcon()!=null){
            imageLoader.displayImage(AppConstants.ICON_TARIFFS_URI_PATH + listTariffsNames.get(position).getTariffIcon(), viewHolder.getTariffIcon());
        }


        return view;
    }


  public   class ViewHolder {

        private ImageView tariffIcon;
        private TextView tariffName;
        private TextView tariffStartPrice;
        private TextView tariffPricePerKm;
        private  RadioButton tariffRadioButton;

        public RadioButton getTariffRadioButton() {
            return tariffRadioButton;
        }

        public void setTariffRadioButton(RadioButton tariffRadioButton) {
            this.tariffRadioButton = tariffRadioButton;
        }

        public TextView getTariffStartPrice() {
            return tariffStartPrice;
        }

        public void setTariffStartPrice(TextView tariffStartPrice) {
            this.tariffStartPrice = tariffStartPrice;
        }

        public TextView getTariffPricePerKm() {
            return tariffPricePerKm;
        }

        public void setTariffPricePerKm(TextView tariffPricePerKm) {
            this.tariffPricePerKm = tariffPricePerKm;
        }

        public ImageView getTariffIcon() {
            return tariffIcon;
        }

        public void setTariffIcon(ImageView tariffIcon) {
            this.tariffIcon = tariffIcon;
        }

        public TextView getTariffName() {
            return tariffName;
        }

        public void setTariffName(TextView tariffName) {
            this.tariffName = tariffName;
        }
    }

}
