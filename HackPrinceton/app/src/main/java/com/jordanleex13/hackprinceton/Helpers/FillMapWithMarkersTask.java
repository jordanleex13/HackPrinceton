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

public class FillMapWithMarkersTask extends AsyncTask<Object, Object, Object> {

    private GoogleMap mMap;
    private HashMap<Marker, String> mHashMap;

    public FillMapWithMarkersTask(GoogleMap map, HashMap<Marker, String> hashMap) {
        mMap = map;
        mHashMap = hashMap;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        Log.e("FillMapTask", "Creating " + EventManager.getSize() + " markers");
        for (int i = 0; i < EventManager.getSize(); i++) {

            Event currEvent = EventManager.getEvent(i);
            float[] coor = currEvent.getCoordinates();

            LatLng latlng = new LatLng(coor[0], coor[1]);
            String title = currEvent.getEventTitle();
            String dateTimeLocal = currEvent.getDatetime_local();
            String url = currEvent.getUrl();

            publishProgress(latlng, title, dateTimeLocal, url);

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);

        LatLng curr = (LatLng) values[0];
        String title = (String) values[1];
        String dateTimeLocal = (String) values[2];

        //String rating = "Rating : " + score;
        String url = (String) values[3];

        Marker temp = mMap.addMarker(new MarkerOptions().position(curr).title(title).snippet(dateTimeLocal));
        Log.e("FillMapTask", "Inserting marker");
        mHashMap.put(temp, url);

    }
}