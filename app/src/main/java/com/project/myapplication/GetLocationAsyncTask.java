package com.project.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

/**
 * Created by sushil on 11/25/16.
 */

@SuppressWarnings("deprecation")
class GetLocationAsyncTask extends AsyncTask<String, String, String> {

    static double lati,longi=0;
    private Context context;

    public GetLocationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
        //Split string to array
        try {
            String[] location = result.split("\\s+");
            //Toast.makeText(context, location[1], Toast.LENGTH_SHORT).show();
            double lat, lon,distance;
            lati = lon = Double.parseDouble(location[0]);
            longi = lat = Double.parseDouble(location[1]);

            if(AppLocalData.isLocationSet(context)) {
                distance = GPSLocation.getDistance(context, lat, lon);
                //Toast.makeText(context, ""+distance, Toast.LENGTH_SHORT).show();
                new Notifier().compareDistance(context, distance);
            }
        } catch (PatternSyntaxException ex) {
            //
        }

    }
}