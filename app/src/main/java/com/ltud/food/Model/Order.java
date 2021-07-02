package com.ltud.food.Model;

import java.util.List;

public class Order {

    private String id, date;
    private long status, payment_method;
    private boolean complete;
    private Restaurant restaurant;
    private List<Order_Food> foodList;

    public Order() {
    }

    public Order(String id, String date, long status, long payment_method, Restaurant restaurant, List<Order_Food> foodList) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.payment_method = payment_method;
        this.restaurant = restaurant;
        this.foodList = foodList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(long payment_method) {
        this.payment_method = payment_method;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Order_Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Order_Food> foodList) {
        this.foodList = foodList;
    }
}
