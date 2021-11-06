package com.ggls.covid19;

import java.util.ArrayList;

public class TravelMap {
    private long id;
    private ArrayList<String> travelLocations;

    public TravelMap(long id, ArrayList<String> travelLocations) {
        this.id = id;
        this.travelLocations = travelLocations;
    }

    public void addLocation(String location) {
        travelLocations.add(location);
    }

    public int getSize() {
        return travelLocations.size();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<String> getTravelLocations() {
        return travelLocations;
    }

    public void setTravelLocations(ArrayList<String> travelLocations) {
        this.travelLocations = travelLocations;
    }
}
