package com.idit.gasomovil;

import java.util.Map;

/**
 * Created by viper on 07/12/2017.
 */

public class FavoriteModel {
    String name;
    //Float premium;
    //Float magna;
    //Float diesel;
    Map<String, Object> Prices;
    int score;
    public String key;

    public FavoriteModel(){

    }

    public FavoriteModel(String name, Map<String, Object> Prices, int score, String key) {
        this.name = name;
        this.Prices = Prices;
        this.score = score;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
