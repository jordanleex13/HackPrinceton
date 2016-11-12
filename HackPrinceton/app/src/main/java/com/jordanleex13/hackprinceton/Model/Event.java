package com.jordanleex13.hackprinceton.Model;

/**
 * Created by Jordan on 2016-11-12.
 */

public class Event {

    int id;
    String title;
    String datetime_local;
    String url;
    String city;
    String[] location;

    float latitude;
    float longitude;

    public Event(int id, String title, String dt, String url, String city, String[] location) {
        this.id = id;
        this.title = title;
        this.datetime_local = dt;
        this.url = url;
        this.city = city;
        this.location = location;

        latitude = Float.valueOf(location[0]);
        longitude = Float.valueOf(location[1]);

    }

    public float[] getCoordinates() {

        float[] coor = {latitude, longitude};

        return coor;
    }
}
