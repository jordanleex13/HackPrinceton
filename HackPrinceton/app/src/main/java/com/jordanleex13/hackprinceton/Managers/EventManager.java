package com.jordanleex13.hackprinceton.Managers;

import com.jordanleex13.hackprinceton.Model.Event;

import java.util.ArrayList;

/**
 * Created by Jordan on 2016-11-12.
 */

public class EventManager {

    private static ArrayList<Event> eventArrayList = new ArrayList<>();

    // Prevent instantiation of class
    private EventManager() {

    }

    public static ArrayList<Event> getList() {
        return eventArrayList;
    }
    public static int getSize() {
        return eventArrayList.size();
    }
    public static void addEvent(Event j) {
        eventArrayList.add(j);
    }
    public static Event getEvent(int index) {
        return eventArrayList.get(index);
    }
    public static void removeEvent(Event j) {
        eventArrayList.remove(j);
    }
    public static void clearList() {
        eventArrayList.clear();
    }



}
