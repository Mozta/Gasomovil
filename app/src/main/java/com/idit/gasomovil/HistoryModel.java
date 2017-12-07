package com.idit.gasomovil;

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
}
