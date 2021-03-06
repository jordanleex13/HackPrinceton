package com.jordanleex13.hackprinceton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.jordanleex13.hackprinceton.Helpers.FillMapWithMarkersTask;
import com.jordanleex13.hackprinceton.Managers.EventManager;

import java.util.HashMap;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private HashMap<Marker, String> markerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        markerHashMap = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng princeton = new LatLng(40.3440, -74.6514);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(princeton, 10.0f));
        mMap.clear();

        mMap.setOnInfoWindowClickListener(this);

        // Fills map with markers of events after a slight delay
        CountDownTimer cd = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new FillMapWithMarkersTask(mMap, markerHashMap).execute();
            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onStop();
        mMap.clear();
        EventManager.clearList();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String markerURL = markerHashMap.get(marker);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(markerURL));
        startActivity(browserIntent);
    }
}
