package com.jordanleex13.hackprinceton.Helpers;

import android.util.Log;

import com.jordanleex13.hackprinceton.Managers.EventManager;
import com.jordanleex13.hackprinceton.Model.Event;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Jordan on 2016-11-12.
 */

public class RunnableParseJson implements Runnable {

    private static final String TAG = RunnableParseJson.class.getSimpleName();

    private String mQuery;

    public RunnableParseJson(String query) {
        mQuery = query;
    }
    @Override
    public void run() {

        try {
            httpGet(mQuery);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void httpGet(String searchTerm) throws IOException, JSONException {

        String in = "https://api.seatgeek.com/2/events/3315199";

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(in));

        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);


            String responseString = out.toString();
            Log.e(TAG, responseString);

            JSONObject reader = new JSONObject(responseString);

            String id = reader.getString("id");
            System.out.println(id);

            String title = reader.getString("title");
            String score = reader.getString("score");
            String url = reader.getString("url");
            String dateTimeLocal = reader.getString("datetime_local");

            JSONObject venue = reader.getJSONObject("venue");

            if (venue != null) {
                String city = venue.getString("city");

                JSONObject location = venue.getJSONObject("location");
                String lat = location.getString("lat");
                String lon = location.getString("lon");
                String locationArr[] = {lat,lon};
                String address = venue.getString("address");

                Event newEvent = new Event(title, dateTimeLocal, url, score, city, locationArr, address);
                EventManager.addEvent(newEvent);
            } else {
                Log.w(TAG, "Could not get venue or location data");
            }
//
//            //Log.e(TAG, "Num of jobs is " + String.valueOf(jsonArray.length()));
//            for(int i=0; i < jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                String id = jsonObject.optString("id");
//                String title = jsonObject.optString("title");
//                String date = jsonObject.optString("datetime_local");
//                String ticket_url = jsonObject.optString("url");
//                String city = jsonObject.optString("city");
//                Log.e(TAG, title);
//                //String[] location = jsonObject.optString("location");
//                //System.out.println(location[0]);
//
//            }



            out.close();
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();

        }
    }
}
