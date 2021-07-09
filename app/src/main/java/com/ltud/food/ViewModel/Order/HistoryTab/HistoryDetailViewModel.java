package com.ltud.food.ViewModel.Order.HistoryTab;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.Order.HistoryTab.HistoryDetailRepository;

import java.util.List;

public class HistoryDetailViewModel extends ViewModel {

    private HistoryDetailRepository repository = HistoryDetailRepository.getInstance();
    private MutableLiveData<Order> orderMutableLiveData;

    public LiveData<Order> getSelectedOrder(String orderID)
    {
        orderMutableLiveData = repository.getSelectedOrder(orderID);
        return orderMutableLiveData;
    }

    public LiveData<String> getOrderID(String resID)
    {
        return  repository.getOrderID(resID);
    }

    public void removeAnOrder(String orderID)
    {
        repository.removeAnOrder(orderID);
    }

    public void createNewOrder(Restaurant restaurant, List<Order_Food> foods)
    {
        repository.createNewOrder(restaurant, foods);
    }
}
