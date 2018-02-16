package com.idit.gasomovil;

/**
 * Created by viper on 11/12/2017.
 */

public class CommentModel {
    String comment;
    double score;
    int timestamp;
    public String key;

    public CommentModel() {
    }

    public CommentModel(String comment, double score, int timestamp, String key) {
        this.comment = comment;
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
}
