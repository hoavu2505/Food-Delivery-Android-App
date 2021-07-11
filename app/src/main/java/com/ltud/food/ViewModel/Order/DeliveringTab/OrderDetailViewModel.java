package com.ltud.food.ViewModel.Order.DeliveringTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.DeliveringTab.OrderDetailRepository;

public class OrderDetailViewModel extends ViewModel {
    private OrderDetailRepository repository = OrderDetailRepository.getInstance();
    private MutableLiveData<Order> orderMutableLiveData;

    public LiveData<Order> getOrder(String orderID)
    {
        orderMutableLiveData = repository.getOrderList(orderID);
        return orderMutableLiveData;
    }

    public void updateCompleteOrder(String orderID, boolean isComplete)
    {
        repository.updateCompleteOrder(orderID, isComplete);
    }
}
