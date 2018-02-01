package com.example.arvind.detailsui;

/**
 * Created by Arvind on 30-01-2018.
 */

public class Blog {
    private String name,rev,rating,date;

    public Blog() {
    }

    public Blog(String name, String rev,String rating,String date) {
        this.name = name;
        this.rev = rev;
        this.rating=rating;
        this.date=date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
