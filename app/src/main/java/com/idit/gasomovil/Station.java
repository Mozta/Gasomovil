package com.idit.gasomovil;

/**
 * Created by viper on 07/12/2017.
 */

public class Station {
    private String name;
    private float score;

    public Station(){

    }

    public Station(String name, float score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}