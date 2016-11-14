package com.project.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sushil on 9/24/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDbb.db";
    private static final String TEST_TABLE_NAME = "testTable";
    private static final String TEST_COLUMN_ID = "_id";
    private static final String TEST_COLUMN_ITEM1 = "item1";
    private static final String TEST_COLUMN_ITEM2 = "item2";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TEST_TABLE_NAME+ " ("+
                        TEST_COLUMN_ID+ " INTEGER PRIMARY KEY, "+
                        TEST_COLUMN_ITEM1+ " TEXT, "+
                        TEST_COLUMN_ITEM2+ " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertItem(String val1, int val2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_COLUMN_ITEM1,val1);
        contentValues.put(TEST_COLUMN_ITEM2,val2);
        db.insert(TEST_TABLE_NAME, null, contentValues);
        return true;
    }
}
