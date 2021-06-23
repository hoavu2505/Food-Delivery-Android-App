package com.ltud.food.Model;

import java.util.ArrayList;

public class Food {
    public String id, restaurant_id,name,rate,img;
    public int price;

    ArrayList<Food> data;

    public ArrayList<Food> getData() {
        return data;
    }

    public Food() {
    }

    public Food(String id, String restaurant_id, String name, String rate, String img, int price) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.rate = rate;
        this.img = img;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
