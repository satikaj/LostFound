package com.example.lostfound.sqlitehelper;

public class Advert {
    int id;
    String name, phone, description, date, locationId, locationName;
    boolean isLost;

    public Advert(int id, String name, String phone, String description, String date, String locationId, String locationName, boolean isLost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.locationId = locationId;
        this.locationName = locationName;
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

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean isLost() {
        return isLost;
    }
}
