package com.project.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sushil on 11/16/16.
 */

public class ScheduleUpdater {
    public static boolean isUpdateAvailable(){
        // Check if new update is available.
        return false;
    }

    public static void update(Context context){
        // Update local database
        DBHelper handler = new DBHelper(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        // Delete all contents of database
        db.execSQL("delete from scheduleTable");

        // Insert items to database
        handler.insertItem(0,"x", "o", "i", "x", "o", "x", "x");
        handler.insertItem(1,"o", "i", "x", "x", "i", "x", "x");
        handler.insertItem(2,"x", "o", "o", "o", "x", "x", "x");
        handler.insertItem(3,"o", "x", "o", "x", "o", "x", "x");
        handler.insertItem(4,"i", "x", "x", "i", "i", "x", "x");
        handler.insertItem(5,"o", "o", "x", "x", "x", "x", "x");
        handler.insertItem(6,"x", "i", "x", "o", "x", "x", "x");
    }

}
