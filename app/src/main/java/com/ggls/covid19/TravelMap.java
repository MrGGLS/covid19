package com.ggls.covid19;


public class TravelMap {
    private String user_name;
    private String province;
    private String city;
    private double latitude;
    private double longitude;

    public TravelMap() {
    }

    public TravelMap(String user_name, String province, String city, double latitude, double longitude) {
        this.user_name = user_name;
        this.province = province;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
