package com.project.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.acl.Group;

/**
 * Created by sushil on 11/16/16.
 */

public class ScheduleUpdater {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void updateSchedule(Context context) {
        // Check if new update is available.
        //TODO remove AppLocalData
        //new AppLocalData().saveUpdateDate(context);
        if (isNetworkAvailable(context)){
            new GetUpdateTimeAsyncTask(context).execute("http://sanzay.com.np/time");
        }else{
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getUpdateDateProcessData(Context context, String time) {
        long appDate = AppLocalData.getUpdateDate(context);
        long serverDate = Long.parseLong(time);
        if (appDate >= serverDate) {
            Toast.makeText(context, "Update not available.", Toast.LENGTH_SHORT).show();
            //new UpdateAsyncTask(context).execute("http://sanzay.com.np/jason1.php");

        }else {
            new UpdateAsyncTask(context).execute("http://sanzay.com.np/jason1.php");
        }
    }

    //Called from async task
    static void updateProcessData(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        try {
            JSONArray updateData = new JSONArray(string);
            updateScheduleDatabase(context,updateData);
        } catch (JSONException e) {
            Toast.makeText(context, "json error", Toast.LENGTH_SHORT).show();

        }
    }

    static void updateScheduleDatabase(Context context,JSONArray jsonArray){
        // Update local database
        DBHelper handler = new DBHelper(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        // Delete all contents of database
        db.execSQL("delete from scheduleTable");

        //Insert items to database
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                handler.insertItem(i,
                        jsonObj.getString("Group1"),
                        jsonObj.getString("Group2"),
                        jsonObj.getString("Group3"),
                        jsonObj.getString("Group4"),
                        jsonObj.getString("Group5"),
                        jsonObj.getString("Group6"),
                        jsonObj.getString("Group7"));

                new AppLocalData().saveUpdateDate(context);
                Toast.makeText(context, "Schedule updated" +
                        "", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public static void update(Context context) {
        // Update local database
        DBHelper handler = new DBHelper(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        // Delete all contents of database
        db.execSQL("delete from scheduleTable");

        // Insert items to database
        handler.insertItem(0, "x", "o", "i", "x", "o", "x", "x");
        handler.insertItem(1, "o", "i", "x", "x", "i", "x", "x");
        handler.insertItem(2, "x", "o", "o", "o", "x", "x", "x");
        handler.insertItem(3, "o", "x", "o", "x", "o", "x", "x");
        handler.insertItem(4, "i", "x", "x", "i", "i", "x", "x");
        handler.insertItem(5, "o", "o", "x", "x", "x", "x", "x");
        handler.insertItem(6, "x", "i", "x", "o", "x", "x", "x");
        AppLocalData.saveUpdateDate(context);
        Toast.makeText(context, "" + AppLocalData.getUpdateDate(context), Toast.LENGTH_SHORT).show();
    }



    @SuppressWarnings("deprecation")
    private static class GetUpdateTimeAsyncTask extends AsyncTask<String, String, String> {

        private Context context;

        public GetUpdateTimeAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            ScheduleUpdater.getUpdateDateProcessData(context, result);
        }
    }

    @SuppressWarnings("deprecation")
    private static class UpdateAsyncTask extends AsyncTask<String, String, String> {

        private Context context;

        public UpdateAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            ScheduleUpdater.updateProcessData(context, result);
        }
    }


}