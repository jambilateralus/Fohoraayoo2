package com.project.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sushil on 11/20/16.
 */

class AppLocalData {
    private static final String PREFS_NAME = "AOP_PREFS";
    private static final String LAT_KEY = "latitude_value";
    private static final String LON_KEY = "longitude_value";


    static void saveLocation(Context context, String latitude, String longitude){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit();
        editor.putString(LAT_KEY, latitude);
        editor.putString(LON_KEY, longitude);
        editor.apply();
    }

    static double getLatitude(Context context){
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(LAT_KEY, null);
        return Double.parseDouble(text);
    }

    static double getLongitude(Context context){
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(LON_KEY, null);
        return Double.parseDouble(text);
    }
}
