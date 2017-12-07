package com.idit.gasomovil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viper on 06/12/2017.
 */

public class HistoryModel {

    String name;
    int timestamp;
    Float price;
    int score;
    public String key;

    public HistoryModel(){

    }

    public HistoryModel(String name, int timestamp, Float price, int score, String key) {
        this.name = name;
        this.timestamp = timestamp;
        this.price = price;
        this.score = score;
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
        result.put("timestamp", timestamp);
        result.put("price", price);
        result.put("score", score);
        result.put("key", key);

        return result;
    }
}
