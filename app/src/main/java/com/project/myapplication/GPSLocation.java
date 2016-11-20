package com.project.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


public class GPSLocation {

    private static Location myLocation, vehicleLocation;


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
     public static void saveCurrentLocation(final Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        // Convert lat and lon to string.
                        String lat = ""+location.latitude;
                        String lon = ""+location.longitude;

                        // Save lat and lon using shared preferences.
                        AppLocalData.saveLocation(context,lat,lon);
                        Log.d("Location", "my location is " + location.longitude+" "+location.latitude);
                        Toast.makeText(context,"Location saved.", Toast.LENGTH_SHORT).show();

                    }
                });
    }



    /** Calculate distance between saved location and vehicle location **/
    public static double getDistance(Context context, double vehichleLat, double vehicleLon){
        myLocation.setLatitude(AppLocalData.getLatitude(context));
        myLocation.setLongitude(AppLocalData.getLongitude(context));
        vehicleLocation.setLatitude(vehichleLat);
        vehicleLocation.setLongitude(vehicleLon);
        return myLocation.distanceTo(vehicleLocation);
    }
}


