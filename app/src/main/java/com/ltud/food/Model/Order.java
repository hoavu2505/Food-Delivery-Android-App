package com.ltud.food.Model;

public class Order {

    private String id;
    private int quantity;
    private String date;
    private long status, payment_method;
    private Food food;

    public Order() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public void setStatus(long status) {
        this.status = status;
    }

    public long getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(long payment_method) {
        this.payment_method = payment_method;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
