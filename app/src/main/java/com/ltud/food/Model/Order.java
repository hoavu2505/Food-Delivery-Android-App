package com.ltud.food.Model;

import java.util.Date;
import java.util.List;

public class Order {

    private String id, location;
    private Date date;
    private long status, payment_method;
    private boolean complete, checked_notification;
    private Restaurant restaurant;
    private List<Order_Food> foodList;

    public Order() {
    }

    public Order(String id, Date date, long status, long payment_method, Restaurant restaurant, List<Order_Food> foodList) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
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

    public boolean isChecked_notify() {
        return checked_notification;
    }

    public void setChecked_notify(boolean checked_notify) {
        this.checked_notification = checked_notify;
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
