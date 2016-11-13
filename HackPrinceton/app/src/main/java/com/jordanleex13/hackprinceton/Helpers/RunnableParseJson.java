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
import org.json.JSONArray;
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

        String ipAddress = Utils.getIPAddress(true);
        String in;

        if (!ipAddress.isEmpty()) {
            in = "https://api.seatgeek.com/2/events?taxonomies.name=" + searchTerm + "&per_page=25&geoip=" + ipAddress + "&range=40mi";
        } else {
            Log.e(TAG, "IP address is empty. Defaulting search");
            in = "https://api.seatgeek.com/2/events?taxonomies.name=" + searchTerm + "&per_page=40";
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(in));

        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);


            String responseString = out.toString();
            Log.e(TAG, responseString);

            JSONObject reader = new JSONObject(responseString);

            JSONArray eventsArray = reader.optJSONArray("events");

            if (eventsArray != null) {

                Log.e(TAG, String.valueOf(eventsArray.length()));
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject curr = eventsArray.getJSONObject(i);


                    String id = curr.getString("id");
                    System.out.println(id);

                    String title = curr.getString("title");
                    String score = curr.getString("score");
                    String url = curr.getString("url");
                    String dateTimeLocal = curr.getString("datetime_local");

                    JSONObject venue = curr.getJSONObject("venue");

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


                }
            }


            out.close();
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();

        }
    }
}
