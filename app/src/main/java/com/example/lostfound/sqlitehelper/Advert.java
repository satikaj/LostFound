package com.example.lostfound.sqlitehelper;

public class Advert {
    int id;
    String name, phone, description, date, location;
    boolean isLost;

    public Advert(int id, String name, String phone, String description, String date, String location, boolean isLost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public boolean isLost() {
        return isLost;
    }
}
