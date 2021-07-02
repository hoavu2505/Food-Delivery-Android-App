package com.ltud.food.Model;

public class Order_Food {

    private String id, name;
    private String img;
    private double price;
    private long rate, quantity;

    public Order_Food() {
    }

    public Order_Food(String id, String name, String image, double price, long rate, long quantity) {
        this.id = id;
        this.name = name;
        this.img = image;
        this.price = price;
        this.rate = rate;
        this.quantity = quantity;
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

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
