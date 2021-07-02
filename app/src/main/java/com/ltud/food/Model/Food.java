package com.ltud.food.Model;

public class Food {

    private String id, name, img;
    private double price;
    private int rate;

    public Food() {
    }

    public Food(String id, String name, String image, double price, int rate) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

}
