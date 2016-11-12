package com.jordanleex13.hackprinceton.Model;

/**
 * Created by Jordan on 2016-11-12.
 */

public class Event {

    private String title;
    private String datetime_local;
    private String url;
    private String scoreStr;
    private String city;
    private String[] location;
    private String address;

    private float latitude;
    private float longitude;
    private float score;

    public Event(String title, String dt, String url, String scoreStr, String city, String[] location, String address) {

        this.title = title;
        this.datetime_local = dt;
        this.url = url;
        this.scoreStr = scoreStr;
        this.city = city;
        this.location = location;
        this.address = address;

        this.latitude = Float.valueOf(location[0]);
        this.longitude = Float.valueOf(location[1]);
        this.score = Float.valueOf(scoreStr);

        printAll();
    }

    public String getEventTitle() {
        return title;
    }
    public String getDatetime_local() {
        return datetime_local;
    }
    public String getUrl() {
        return url;
    }
    public float[] getCoordinates() {
        float[] coor = {latitude, longitude};
        return coor;
    }

    private void printAll() {
        System.out.println(title + datetime_local + url + score  + "\t" + latitude + "\t" + longitude + "\t" + address);
    }
}
