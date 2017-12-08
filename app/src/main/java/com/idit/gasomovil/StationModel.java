package com.idit.gasomovil;

import java.util.Map;

/**
 * Created by viper on 08/12/2017.
 */

public class StationModel {
    Float latitude;
    Float longitude;
    String name;
    Float score;
    Map<String, Object> comments;
    Map<String, Object> prices;
    public String key;

    public StationModel() {
    }

    public StationModel(Float latitude, Float longitude, String name, Float score, Map<String, Object> comments, Map<String, Object> prices, String key) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.score = score;
        this.comments = comments;
        this.prices = prices;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
