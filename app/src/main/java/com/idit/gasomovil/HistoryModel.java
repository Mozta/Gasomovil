package com.idit.gasomovil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viper on 06/12/2017.
 */

public class HistoryModel {

    String name;
    Float liters;
    Float price;
    int score;
    int timestamp;

    public String key;

    public HistoryModel(){

    }

    public HistoryModel(String name, Float liters, Float price, int score, int timestamp, String key) {
        this.name = name;
        this.liters = liters;
        this.price = price;
        this.score = score;
        this.timestamp = timestamp;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("liters", liters);
        result.put("price", price);
        result.put("score", score);
        result.put("timestamp", timestamp);
        result.put("key", key);

        return result;
    }
}
