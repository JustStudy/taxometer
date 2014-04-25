package com.developer.taxometer.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by Ruslik on 31.03.14.
 */
public class AppDialog extends Dialog {
    private Activity activity;
    public AppDialog(Activity context, int theme) {
        super(context, theme);

        this.activity = context;

    }

    @Override
    public void onBackPressed() {
        activity.onBackPressed();
        super.onBackPressed();
    }
}
