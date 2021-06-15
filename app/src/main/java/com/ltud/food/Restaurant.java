package com.ltud.food;

public class Restaurant {

    public String id, name, address, img;
    public double rate;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String address, String img, double rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.img = img;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
