package com.ltud.food.ViewModel.RestaurantDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.RestaurantDetail.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository = CartRepository.getInstance();
    private MutableLiveData<Order> orderLiveData;

    public LiveData<Order> getCurrentOrder(String orderID)
    {
        orderLiveData = cartRepository.getCurrentOrder(orderID);
        return orderLiveData;
    }

    public void updateFoodQuantity(String orderID, List<Order_Food> foodList)
    {
        cartRepository.updateFoodQuantity(orderID, foodList);
        Order order = orderLiveData.getValue();
        order.setFoodList(foodList);
        orderLiveData.setValue(order);
    }

    public void deleteOneFood(String orderID, int index, Order_Food food)
    {
        cartRepository.deleteOneFood(orderID, food);
        Order order= orderLiveData.getValue();
        order.getFoodList().remove(index);
        orderLiveData.setValue(order);
    }

    public void deleteOneOrder(String orderID)
    {
        cartRepository.deleteOneOrder(orderID);
        Order order = orderLiveData.getValue();
        order.getFoodList().clear();
        orderLiveData.setValue(order);
    }

}
