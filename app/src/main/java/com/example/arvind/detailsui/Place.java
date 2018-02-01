package com.example.arvind.detailsui;



public class Place {

    public Double lat;
    public Double lon;
    public String title;

    public Place() {
    }

    public Place(Double lat, Double lon, String title) {
        this.lat = lat;
        this.lon = lon;
        this.title = title;


    }

    public String toString(){
        return this.lat + " " + this.lon + " " + this.title;
    }
}
