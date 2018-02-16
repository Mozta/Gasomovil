package com.idit.gasomovil;

import java.util.Map;

/**
 * Created by viper on 08/12/2017.
 */

public class StationModel {
    Float latitude;
    Float longitude;
    String name;
    String address;
    Float score;
    String phone;
    Map<String, Object> Comments;
    Map<String, Object> Prices;
    public String key;

    public StationModel() {
    }

    public StationModel(Float latitude, Float longitude, String name, String address, Float score, String phone, Map<String, Object> Comments, Map<String, Object> Prices, String key) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.score = score;
        this.phone = phone;
        this.Comments = Comments;
        this.Prices = Prices;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
