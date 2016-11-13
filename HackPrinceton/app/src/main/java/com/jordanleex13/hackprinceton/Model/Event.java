package com.jordanleex13.hackprinceton.Model;

import android.util.Log;

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

        dateTimeFormatter(dt);

        this.title = title;
        this.url = url;
        this.scoreStr = scoreStr;
        this.city = city;
        this.location = location;
        this.address = address;

        this.latitude = Float.valueOf(location[0]);
        this.longitude = Float.valueOf(location[1]);
        if (Float.valueOf(scoreStr) != null){
            this.score = Float.valueOf(scoreStr);
        }
        else{
            this.score = 0;
        }

        printAll();
    }


    /**
     * Parse time and display in a more readable format
     * @param dt            dt in form yyyy-mm-dd
     */
    private void dateTimeFormatter(String dt) {

        String delims = "[-]";
        String[] tokens = dt.split(delims);

        String month = getMonthFromNum(tokens[1]);
        String delims2 = "[T]";
        String[] temp = tokens[2].split(delims2);

        datetime_local = month + " " + temp[0] + ", " + tokens[0] + " @ " + temp[1];
        Log.e("EVENT", datetime_local);
    }
    private String getMonthFromNum(String num) {
        int caseNum = Integer.valueOf(num);
        switch (caseNum) {
            case 1: return "Januray";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "Unknown month";
        }
    }


    /**
     * Getters
     */
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
