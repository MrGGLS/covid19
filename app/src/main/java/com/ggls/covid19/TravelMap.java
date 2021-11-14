package com.ggls.covid19;

import java.util.ArrayList;

public class TravelMap {
    private int id;
    private int user_id;
    private String province;
    private String city;
    private String country;

    public TravelMap() {
    }

    public TravelMap(int id, int user_id, String province, String city, String country) {
        this.id = id;
        this.user_id = user_id;
        this.province = province;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
