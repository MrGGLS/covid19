package com.ggls.covid19;


public class TravelMap {
    private int id;
    private String province;
    private String city;
    private double latitude;
    private double longitude;

    public TravelMap() {
    }

    public TravelMap(int id, String province, String city, double latitude, double longitude) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
