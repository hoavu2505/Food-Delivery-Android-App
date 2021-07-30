package com.ltud.food.Model;

public class Favourite {
    private String id;
    private boolean check_fav = false;
    private Restaurant restaurant;

    public Favourite() {
    }

    public Favourite(String id, boolean check_fav, Restaurant restaurant) {
        this.id = id;
        this.check_fav = check_fav;
        this.restaurant = restaurant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheck_fav() {
        return check_fav;
    }

    public void setCheck_fav(boolean check_fav) {
        this.check_fav = check_fav;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
