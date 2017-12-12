package com.idit.gasomovil;

/**
 * Created by viper on 11/12/2017.
 */

public class CommentModel {
    String comment;
    //float score;
    public String key;

    public CommentModel() {
    }

    public CommentModel(String comment, String key) {
        this.comment = comment;
        //this.score = score;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
