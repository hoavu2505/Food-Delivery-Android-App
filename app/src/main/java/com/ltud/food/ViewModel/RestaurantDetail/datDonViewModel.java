package com.ltud.food.ViewModel.RestaurantDetail;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.RestaurantDetail.datDonRepository;

import java.util.ArrayList;
import java.util.List;

public class datDonViewModel extends ViewModel {

    private final datDonRepository repository = datDonRepository.getInstance();
    private MutableLiveData<Order> currentOrderLiveData;

    public LiveData<Order> getCurrentOrder(String resID)
    {
        currentOrderLiveData = repository.getCurrentOrder(resID);
        return currentOrderLiveData;
    }

    public void addOneOrder(Restaurant restaurant, Order_Food food)
    {
        Order order = repository.addOneOrder(restaurant, food);
        currentOrderLiveData.setValue(order);
    }

    public void addFood(String orderID, Order_Food food)
    {
        repository.addFood(orderID, food);
        Order order = currentOrderLiveData.getValue();
        order.getFoodList().add(food);
        currentOrderLiveData.setValue(order);
    }

    public void updateFoodQuantity(String orderID, List<Order_Food> foodList)
    {
        repository.updateFoodQuantity(orderID, foodList);
        Order order = currentOrderLiveData.getValue();
        order.setFoodList(foodList);
        currentOrderLiveData.setValue(order);
    }

}
