package com.project.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by sushil on 11/20/16.
 */

class AppLocalData {
    private static final String PREFS_NAME = "AOP_PREFS";
    private static final String LAT_KEY = "latitude_value";
    private static final String LON_KEY = "longitude_value";
    private static final String DATE_KEY = "date_value";


    /**
     * For saving and reading location
     **/
    static void saveLocation(Context context, String latitude, String longitude) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit();
        editor.putString(LAT_KEY, latitude);
        editor.putString(LON_KEY, longitude);
        editor.apply();
    }

    static double getLatitude(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(LAT_KEY, null);
        return Double.parseDouble(text);
    }

    static double getLongitude(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(LON_KEY, null);
        return Double.parseDouble(text);
    }

    static boolean locationIsSaved(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(LAT_KEY, "0");
        if (Double.parseDouble(text)>0){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * For saving and reading update date
     **/
    static void saveUpdateDate(Context context) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        String fullTime = "" + year + month + day + hr + min + sec;

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit();
        editor.putString(DATE_KEY, fullTime);
        editor.apply();
    }

    static long  getUpdateDate(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(DATE_KEY, "0");
        return Long.parseLong(text);
    }

    static boolean isLocationSet(Context context){
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.contains(LAT_KEY);
    }

    static double getDistanceSavedForNotification(Context context){
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getFloat("distance",0);
    }
}
