package com.ltud.food.ViewModel.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Home.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository = CartRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getOrderList()
    {
        if(orderListLiveData == null)
        {
            orderListLiveData = cartRepository.getOrderListLiveData();
        }
        return orderListLiveData;
    }

    public void updateOrderQuantity(String orderID, int index, int quantity)
    {
        cartRepository.updateOrderQuantity(orderID, quantity);
        List<Order> orders = orderListLiveData.getValue();
        orders.get(index).setQuantity(quantity);
        orderListLiveData.setValue(orders);
    }

    public void deleteOneOrder(String orderID, int index)
    {
        cartRepository.deleteOneOrder(orderID);
        List<Order> orders = orderListLiveData.getValue();
        orders.remove(index);
        orderListLiveData.setValue(orders);
    }
}
