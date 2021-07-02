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
    private MutableLiveData<List<Order_Food>> orderListLiveData;

    public LiveData<List<Order_Food>> getOrderList(String orderID)
    {
        if(orderListLiveData == null)
        {
            orderListLiveData = cartRepository.getOrderListLiveData(orderID);
        }
        return orderListLiveData;
    }

    public void updateFoodQuantity(String orderID, List<Order_Food> foodList)
    {
        cartRepository.updateFoodQuantity(orderID, foodList);
        orderListLiveData.setValue(foodList);
    }

    public void deleteOneFood(String orderID, int index, Order_Food food)
    {
        cartRepository.deleteOneFood(orderID, food);
        List<Order_Food> orders = orderListLiveData.getValue();
        orders.remove(index);
        orderListLiveData.setValue(orders);
    }

    public void deleteOneOrder(String orderID)
    {
        cartRepository.deleteOneOrder(orderID);
        List<Order_Food> orders = orderListLiveData.getValue();
        orders.clear();
        orderListLiveData.setValue(orders);
    }

    public LiveData<Restaurant> getRestaurant(String orderID)
    {
        return cartRepository.getRestaurant(orderID);
    }

}
