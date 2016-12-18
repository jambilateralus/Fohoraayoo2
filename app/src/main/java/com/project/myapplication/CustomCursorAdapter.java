package com.project.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by sushil on 9/24/16.
 */

class CustomCursorAdapter extends CursorAdapter {

    private int tabPosition;
    private Calendar calendar = Calendar.getInstance();
    private int day = calendar.get(Calendar.DAY_OF_WEEK);
    CustomCursorAdapter(Context context, Cursor cursor, int tabPosition){
        super(context,cursor,0);
        this.tabPosition = tabPosition;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView comp1 = (TextView) view.findViewById(R.id.component1);
        TextView comp2 = (TextView) view.findViewById(R.id.component2);
       // RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

        // Extract properties from cursor
        String cmp = "item"+tabPosition;
        final String vehicleType = cursor.getString(cursor.getColumnIndexOrThrow(cmp));
        int pos = cursor.getInt(cursor.getColumnIndexOrThrow("item0"));


        // Populate fields with extracted properties
        comp1.setText(getDay(pos));
        comp2.setText(getVehicleType(vehicleType));

        // Highlight the current day
        if(day-1== pos){
            //relativeLayout.setBackgroundColor(-1);
            comp1.setTextColor(0xff0000ff);
            comp2.setTextColor(0xffff00ff);

        }


    }

    private String getVehicleType(String x){
        switch (x){
            case "o":
                return "Organic";
            case "i":
                return "Inorganic";
            case "x":
                return "--";
        }
        return null;
    }

    private String getDay(int position) {
        switch (position) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return null;
    }

}
