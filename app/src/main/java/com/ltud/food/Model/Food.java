package com.ltud.food.Model;

import android.net.Uri;

public class Food {

    private String id, name;
    private String img;
    private long price;
    private long rate;

    public Food() {
    }

    public Food(String id, String name, String image, long price, long rate) {
        this.id = id;
        this.name = name;
        this.img = image;
        this.price = price;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }
}
