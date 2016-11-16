package com.project.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


public class GPSLocation {

    /** Check if GPS is turned on or not.
     * This function returns:
     * True if GPS is on and False if GPS is off */
    public static boolean gpsIsOn(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /** Open Location settings
     * so user can turn on GPS. */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /** Get latitude and longitude of current location.
     * This function returns: LatLng object. **/
     private static void getCurrentLocationLatLng(final Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.longitude+" "+location.latitude);
                        Toast.makeText(context, " "+location.longitude+" "+location.latitude, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    /** Function to test above methods */
    public static void getCurrentLocation(Context context) {
        if (!gpsIsOn(context)){
            Toast.makeText(context, "OFF", Toast.LENGTH_SHORT).show();
            openGpsSettings(context);
        }else {
            getCurrentLocationLatLng(context);
        }
    }
}


