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
    private static final String TEST_TABLE_NAME = "scheduleTable";
    private static final String TEST_COLUMN_ID = "_id";
    private static final String COLUMN_ITEM0 = "item0";
    private static final String COLUMN_ITEM1 = "item1";
    private static final String COLUMN_ITEM2 = "item2";
    private static final String COLUMN_ITEM3 = "item3";
    private static final String COLUMN_ITEM4 = "item4";
    private static final String COLUMN_ITEM5 = "item5";
    private static final String COLUMN_ITEM6 = "item6";
    private static final String COLUMN_ITEM7 = "item7";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TEST_TABLE_NAME+ " ( "+
                        TEST_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_ITEM0+ " INTEGER, "+
                        COLUMN_ITEM1+ " TEXT, "+
                        COLUMN_ITEM2+ " TEXT, "+
                        COLUMN_ITEM3+ " TEXT, "+
                        COLUMN_ITEM4+ " TEXT, "+
                        COLUMN_ITEM5+ " TEXT, "+
                        COLUMN_ITEM6+ " TEXT, "+
                        COLUMN_ITEM7+ " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TEST_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem(int val0, String val1, String val2, String val3, String val4, String val5, String val6, String val7){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM0,val0);
        contentValues.put(COLUMN_ITEM1,val1);
        contentValues.put(COLUMN_ITEM2,val2);
        contentValues.put(COLUMN_ITEM3,val3);
        contentValues.put(COLUMN_ITEM4,val4);
        contentValues.put(COLUMN_ITEM5,val5);
        contentValues.put(COLUMN_ITEM6,val6);
        contentValues.put(COLUMN_ITEM7,val7);
        db.insert(TEST_TABLE_NAME, null, contentValues);
        return true;
    }
}
