package com.developer.taxometer.utils;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by developer on 15.04.14.
 */
public class TimerUtils {


    public TimerUtils(String stringTextViewResource, TextView textView) {
        this.textView = textView;
        this.stringTextViewResource = stringTextViewResource;
    }

    public TimerUtils() {
    }

    private long startTime;
    private TextView textView;
    private long timeInMillies;
    private Handler myHandler = new Handler();
    private long finalTime;
    private  long timeSwap;
 private String stringTextViewResource;
    public static String millisecondsTimeToString(long time){
        int seconds = (int) (time/ 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (time % 1000);
    //  String totalString =("" + minutes + ":"  + String.format("%02d", seconds) + ":" + String.format("%03d", milliseconds));
        String totalString =(String.format("%02d", minutes)+ ":"  + String.format("%02d", seconds));
        return totalString;
    }

    public  void startTimer() {
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    public  void stopTimer() {

        myHandler.removeCallbacks(updateTimerMethod);
    }

    public void pauseTimer(){

     timeSwap += timeInMillies;
      stopTimer();
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            //int milliseconds = (int) (finalTime % 1000);
            if(textView !=null) {

             ///  textView.setText("" + minutes + ":" + String.format("%02d", seconds) + ":" + String.format("%03d", milliseconds));
                textView.setText(stringTextViewResource+" "+String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            myHandler.postDelayed(this, 0);
        }

    };

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(String stringTextViewResource, TextView textView) {
        this.textView = textView;
        this.stringTextViewResource = stringTextViewResource;
    }

    public long getTimeInMillies() {
        return timeInMillies;
    }

    public void setTimeInMillies(long timeInMillies) {
        this.timeInMillies = timeInMillies;
    }

    public long getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(long finalTime) {
        this.finalTime = finalTime;
    }

    public long getTimeSwap() {
        return timeSwap;
    }

    public void setTimeSwap(long timeSwap) {
        this.timeSwap = timeSwap;
    }
}
