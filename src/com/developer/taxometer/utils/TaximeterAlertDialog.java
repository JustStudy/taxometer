package com.developer.taxometer.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.developer.taxometer.R;
import com.developer.taxometer.services.LocationService;

/**
 * Created by developer on 09.04.14.
 */
public class TaximeterAlertDialog {

    public static AlertDialog alert;
    public static Handler alertHandler = new Handler();


    public static Runnable dialogRunnable = new Runnable() {
        @Override
        public void run() {
            if ((alert != null) && (alert.isShowing())) {
                alert.dismiss();
            }
        }
    };

    public static void showTaximeterAlertDialog(Context activity, String titleMessage, String message, boolean cancelable, final int timerToCancel) {

        View view = View.inflate(activity, R.layout.alert_dialog_layout, null);
        TextView textViewForText = (TextView) view.findViewById(R.id.textViewAlertDialogText);
        textViewForText.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titleMessage)
                .setCancelable(cancelable)
                .setView(view);


        alert = builder.create();
        alert.show();
        alertHandler.postDelayed(dialogRunnable, timerToCancel);


       /* alertHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((alert != null) && (alert.isShowing())) {
                    alert.dismiss();
                }
            }
        },timerToCancel);*/

    }

    public static void showExitAlertDialog(final SherlockActivity activity, final Class SherlockActivity) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.alert_dialog_exit_dialog));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(activity.getResources().getString(R.string.alert_dialog_exit_question));

        alertDialog.setPositiveButton(activity.getString(R.string.alert_dialog_text_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.finish();
                if (LocationService.locationService != null) {
                   // activity.stopService(new Intent(activity, LocationService.class));

                    LocationService.locationService.clearServiceRouteData();

                }
                activity.startActivity(new Intent(activity, SherlockActivity));
            }
        });

        alertDialog.setNegativeButton(activity.getString(R.string.alert_dialog_text_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void showExitAlertDialog(final SherlockActivity activity) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.alert_dialog_exit_dialog));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(activity.getResources().getString(R.string.alert_dialog_exit_question));

        alertDialog.setPositiveButton(activity.getString(R.string.alert_dialog_text_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.finish();

                if(LocationService.locationService!=null){
                    LocationService.locationService.clearServiceRouteData();

                }
                /*if (LocationService.locationService != null) {
                    activity.stopService(new Intent(activity, LocationService.class));
                }*/

            }
        });

        alertDialog.setNegativeButton(activity.getString(R.string.alert_dialog_text_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
