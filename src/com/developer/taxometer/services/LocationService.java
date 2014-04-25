package com.developer.taxometer.services;


import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import com.developer.taxometer.TaximeterApplication;
import com.developer.taxometer.models.Route;
import com.developer.taxometer.models.TariffModel;
import com.developer.taxometer.utils.Filters;
import com.developer.taxometer.utils.PreferenceHelper;
import com.developer.taxometer.utils.TimerUtils;


import java.util.ArrayList;

/**
 * Created by developer on 24.03.14.
 */

public class LocationService extends Service implements LocationListener {


    public static LocationService locationService;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private Location location;

    ///coordinate values
    public static double latitude;
    public static double longitude;
    public static double secondLongitude;
    public static double secondLatitude;
    public static double thirdLongitude;
    public static double thirdLatitude;


    public static float totalDistance = 0;
    public static double anotherTotalDistance = 0;
    public static double thirdDistance = 0;

    private long mLastLocationMillis;
    private Location mLastLocation;
    private boolean isGPSFix;

    private int count = 0;
    public static boolean START_LOCATION_DATA = false;


    Location previousPoint;
    Location nextPoint;

 /*   GeoPoint geoPointPrev;
    GeoPoint geoPointNext;
*/
    Location thirdPrevPoint;
    Location thirdNextPoint;

    private String taxiCurrency;

    ///route data
    private Route currentRoute = new Route();
    private TariffModel currentTariffModel;
    private TimerUtils timerUtilsWholeTime = new TimerUtils();
    private TimerUtils timerUtilsWaitingTime = new TimerUtils();
    public boolean WHOLE_TIMER_IS_RUNNING;
    public boolean WAIT_TIMER_IS_RUNNING;
    public ArrayList<TariffModel> arrayListTariffs = new ArrayList<TariffModel>();




    public void clearServiceRouteData() {

        currentRoute = new Route();
        currentTariffModel = null;
        timerUtilsWaitingTime = new TimerUtils();
        timerUtilsWholeTime = new TimerUtils();
        arrayListTariffs = new ArrayList<TariffModel>();
        latitude = 0;
        longitude = 0;
        totalDistance = 0;
        secondLongitude =0;
        secondLatitude =0;
        anotherTotalDistance = 0;
        thirdDistance = 0;
        previousPoint = null;
        nextPoint = null;
      //  geoPointPrev = null;
       // geoPointNext = null;
        thirdNextPoint = null;
        thirdPrevPoint = null;
        count =0;
        START_LOCATION_DATA =false;
        WHOLE_TIMER_IS_RUNNING =false;
        WAIT_TIMER_IS_RUNNING = false;
    }

    public void setTaxiCurrency(String taxiCurrency) {
        this.taxiCurrency = taxiCurrency;
    }

    public String getTaxiCurrency() {
        return taxiCurrency;
    }

    public TimerUtils getTimerUtilsWholeTime() {
        return timerUtilsWholeTime;
    }

    public TimerUtils getTimerUtilsWaitingTime() {
        return timerUtilsWaitingTime;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }

    public TariffModel getCurrentTariffModel() {
        return currentTariffModel;
    }

    public void setCurrentTariffModel(TariffModel currentTariffModel) {
        this.currentTariffModel = currentTariffModel;
    }

    public static double getThirdDistance() {
        return thirdDistance;
    }

    public static double getAnotherTotalDistance() {
        return anotherTotalDistance;
    }

    public static float getTotalDistance() {
        return totalDistance;
    }

    protected LocationManager locationManager;

    private int gpsUpdateFrequency;
    private float gpsUpdateDistance;

    public int getGpsUpdateFrequency() {
        return gpsUpdateFrequency;
    }

    public float getGpsUpdateDistance() {
        return gpsUpdateDistance;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) TaximeterApplication.appContext.getSystemService(LOCATION_SERVICE);


            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                //&& !isNetworkEnabled) {
                //   showSettingsAlert();

            } else {
                this.canGetLocation = true;

             /*   if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            AppConstants.MIN_FREQUENCY_VALUE_TO_UPDATE_GPS_STATUS, AppConstants.MIN_DISTANCE_TO_UPDATE_GPS_STATUS, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }*/

                if (isGPSEnabled) {

                    if (location == null) {

                        gpsUpdateDistance = PreferenceHelper.readUpdateGpsDistance(this);
                        gpsUpdateFrequency = PreferenceHelper.readUpdateGpsTimeFrequency(this);

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsUpdateFrequency,
                                gpsUpdateDistance, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }


    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationService.this);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        locationService = this;
        taxiCurrency = PreferenceHelper.readCurrencyFromPreference(this);
        getLocation();
        Log.e("locationService", "onCreate()");
        Log.e(LocationService.class.getCanonicalName()," taxiCurrency "+taxiCurrency);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationService = null;
        stopUsingGPS();
     //   sendBroadcastToActivity(Filters.FLAG_STOP_MONITORING);
        Log.e("locationService", "onDestroy()");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    @Override
    public void onLocationChanged(Location location) {

        if (START_LOCATION_DATA) {

            count++;

            if (count > 2) {

                mLastLocation = location;
                mLastLocationMillis = SystemClock.elapsedRealtime();

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                secondLatitude = location.getLatitude();
                secondLongitude = location.getLongitude();
                Log.e(LocationService.class.getCanonicalName(),"get longitude "+ longitude+" latitude "+latitude);
                calculateDistanceBetweenPoints();
              //  calculateSecondDistance();
                calculateThirdDistance();
              //  sendBroadcastToActivity(Filters.FLAG_MONITORING);
            }
        } else {
            clearServiceData();
        }


    }

    private void calculateDistanceBetweenPoints() {


        if (previousPoint == null) {

            double prefLatitude = latitude / 1e6;
            double prefLongitude = longitude / 1e6;
            previousPoint = new Location("start point");
            previousPoint.setLatitude(prefLatitude);
            previousPoint.setLongitude(prefLongitude);


        } else {
            double nextLatitude = latitude / 1e6;
            double nextLongitude = longitude / 1e6;
            nextPoint = new Location("finish point");
            nextPoint.setLatitude(nextLatitude);
            nextPoint.setLongitude(nextLongitude);
            float distance = previousPoint.distanceTo(nextPoint);
            totalDistance = totalDistance + distance;
            //refresh start point
            previousPoint.setLatitude(nextLatitude);
            previousPoint.setLongitude(nextLongitude);

        Log.e(LocationService.class.getCanonicalName(),"first distance "+totalDistance);
        }

    }



 /*   private void calculateSecondDistance() {

        if (geoPointPrev == null) {
            int preLatitude = (int) (latitude * 1e6);
            int preLongitude = (int) (longitude * 1e6);
            geoPointPrev = new GeoPoint(preLatitude, preLongitude);
        } else {
            int nextLatitude = (int) (latitude * 1e6);
            int nextLongitude = (int) (longitude * 1e6);
            geoPointNext = new GeoPoint(nextLatitude, nextLongitude);
            double takenDistance = CalculationByDistance(geoPointPrev, geoPointNext);
            anotherTotalDistance = anotherTotalDistance + takenDistance;
            geoPointPrev = new GeoPoint(nextLatitude, nextLongitude);

        }

    }*/

    private void calculateThirdDistance() {

        if (thirdPrevPoint == null) {

            thirdPrevPoint = new Location("A");
            double preThirdLat = secondLatitude;
            double preThirdLong = secondLongitude;
            thirdPrevPoint.setLatitude(preThirdLat);
            thirdPrevPoint.setLongitude(preThirdLong);
        } else {

            // third method
            thirdNextPoint = new Location("B");
            double nextThirdLat = secondLatitude;
            double nextThirdLong = secondLongitude;
            thirdNextPoint.setLongitude(nextThirdLong);
            thirdNextPoint.setLatitude(nextThirdLat);
            thirdDistance = thirdDistance + distance(thirdPrevPoint.getLatitude(), thirdPrevPoint.getLongitude(), thirdNextPoint.getLatitude(), thirdNextPoint.getLongitude(), 'K');
            //refresh startPoint
            thirdPrevPoint.setLatitude(nextThirdLat);
            thirdPrevPoint.setLongitude(nextThirdLong);
Log.e(LocationService.class.getCanonicalName(),"another distance "+thirdDistance);
        }


    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /*public double CalculationByDistance(GeoPoint StartP, GeoPoint EndP) {
        double buf = (double) StartP.getLatitudeE6();
        // double lat1 = StartP.getLatitudeE6()/1E6;
        double lat1 = buf / 1e6;
        double lat2 = EndP.getLatitudeE6() / 1e6;

        double lon1 = StartP.getLongitudeE6() / 1e6;

        double lon2 = EndP.getLongitudeE6() / 1e6;

        double dLat = Math.toRadians(lat2 - lat1);

        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +

                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *

                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * 6378.1;

    }*/

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendBroadcastToActivity(int flag) {
        Bundle bundle = new Bundle();

        if (flag == Filters.FLAG_MONITORING) {
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_LATITUDE, latitude);
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_LONGITUDE, longitude);
            bundle.putFloat(Filters.INTENT_FLAG_VALUE_DISTANCE, totalDistance);
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_ANOTHER_DISTANCE, anotherTotalDistance);
            bundle.putDouble(Filters.INTENT_FLAG_THIRD_DISTANCE, thirdDistance);
            Intent intent = new Intent(Filters.BROADCAST_FILTER_REFRESH_COORD);
            intent.setAction(Filters.BROADCAST_FILTER_REFRESH_COORD);
            intent.putExtras(bundle);
            sendBroadcast(intent);
            Log.e("sendBroadcast", "" + latitude + ", " + longitude + " total distance " + totalDistance);
        }
        if (flag == Filters.FLAG_STOP_MONITORING) {
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_LATITUDE, 0);
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_LONGITUDE, 0);
            bundle.putFloat(Filters.INTENT_FLAG_VALUE_DISTANCE, totalDistance);
            bundle.putDouble(Filters.INTENT_FLAG_VALUE_ANOTHER_DISTANCE, anotherTotalDistance);
            Intent intent = new Intent(Filters.BROADCAST_FILTER_REFRESH_COORD);
            intent.setAction(Filters.BROADCAST_FILTER_REFRESH_COORD);
            intent.putExtras(bundle);
            sendBroadcast(intent);
            Log.e("sendBroadcast", "" + 0 + ", " + 0 + " total distance " + totalDistance);
        }


    }


    private void clearServiceData() {

        previousPoint = null;
        nextPoint = null;
        //geoPointNext = null;
       // geoPointPrev = null;
        thirdPrevPoint = null;
        thirdNextPoint = null;
        totalDistance = 0;
        latitude = 0;
        longitude = 0;
        secondLongitude =0;
        secondLatitude =0;
        anotherTotalDistance = 0;
        thirdDistance = 0;
    }



    private class MyGPSListener implements GpsStatus.Listener {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (mLastLocation != null)
                        isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;

                    if (isGPSFix) { // A fix has been acquired.
                        // Do something.
                    } else { // The fix has been lost.
                        // Do something.
                    }

                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    // Do something.
                    isGPSFix = true;

                    break;
            }
        }
    }


}
