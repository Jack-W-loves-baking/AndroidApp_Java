package com.example.dmsassignment4;

import java.io.Serializable;

/**
 * This class is to serialize json object.
 */
public class User implements Serializable {

    private int ID;
    private double latitude;
    private double longitude;
    private String title;
    private String username;

    public User (int id, double latitude, double longitude, String title){
        this.setID(id);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTitle(title);
    }

    //getters and setters for private variables.
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
