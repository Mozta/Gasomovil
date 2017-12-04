package com.idit.gasomovil;

/**
 * Created by viper on 01/12/2017.
 */

public class User {
    //private String uid;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String brand;
    private String model;
    private int year;
    private String serie;

    public User(){
    }

    public User(String name, String last_name, String email, String brand, String model, int year, String serie) {
        //this.uid = uid;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        //this.password = password;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.serie = serie;
    }

    /*
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
