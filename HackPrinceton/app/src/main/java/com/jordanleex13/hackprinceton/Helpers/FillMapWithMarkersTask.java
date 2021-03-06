package com.jordanleex13.hackprinceton.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jordanleex13.hackprinceton.Managers.EventManager;
import com.jordanleex13.hackprinceton.Model.Event;

import java.util.HashMap;

/**
 * Created by Jordan on 2016-11-12.
 */

/**
 * AsyncTask to fill out the Google Map with markers of different events
 */
public class FillMapWithMarkersTask extends AsyncTask<Object, Object, Object> {

    private GoogleMap mMap;
    private HashMap<Marker, String> mHashMap;

    /**
     * Constructor for async task
     * @param map           the google map
     * @param hashMap       a hashmap to relate an event marker with its corresponding url
     */
    public FillMapWithMarkersTask(GoogleMap map, HashMap<Marker, String> hashMap) {
        mMap = map;
        mHashMap = hashMap;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        Log.e("FillMapTask", "Creating " + EventManager.getSize() + " markers");

        // Take arraylist of events and create a marker
        for (int i = 0; i < EventManager.getSize(); i++) {

            Event currEvent = EventManager.getEvent(i);

            // Coordinates
            float[] coor = currEvent.getCoordinates();
            LatLng latlng = new LatLng(coor[0], coor[1]);

            // Other info
            String title = currEvent.getEventTitle();
            String dateTimeLocal = currEvent.getDatetime_local();
            String url = currEvent.getUrl();

            Log.d("FillMapTask", title);

            publishProgress(latlng, title, dateTimeLocal, url);
            // Optimization to display all markers
            try {
                Log.d("FillMapTask", "sleeping thread");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);

        LatLng curr = (LatLng) values[0];
        String title = (String) values[1];
        String dateTimeLocal = (String) values[2];
        String url = (String) values[3];

        // Add marker to map
        Marker temp = mMap.addMarker(new MarkerOptions().position(curr).title(title).snippet(dateTimeLocal));
        Log.e("FillMapTask", "Inserting marker");
        mHashMap.put(temp, url);

    }
}