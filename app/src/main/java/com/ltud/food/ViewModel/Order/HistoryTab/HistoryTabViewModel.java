package com.ltud.food.ViewModel.Order.HistoryTab;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.HistoryTab.HistoryTabRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryTabViewModel extends ViewModel {

    private HistoryTabRepository repository = HistoryTabRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getHistoryOrderList()
    {
        orderListLiveData = repository.getHistoryOrderList();
        return orderListLiveData;
    }

    public LiveData<List<Order>> getDateOrderFilter(Date date)
    {
        orderListLiveData = repository.getDateOrderFilter(date);
        return orderListLiveData;
    }

    public LiveData<List<Order>> getStatusOrderFilter(boolean isComplete)
    {
        orderListLiveData = repository.getStatusOrderFilter(isComplete);
        return orderListLiveData;
    }

    public LiveData<List<Order>> getOrderFilter(boolean isComplete, Date date)
    {
        orderListLiveData = repository.getOrderFilter(isComplete, date);
        return orderListLiveData;
    }

    public void removeAnOrder(String orderID, int position)
    {
        repository.removeAnOrder(orderID);
        List<Order> orderList = orderListLiveData.getValue();
        orderList.remove(position);
        orderListLiveData.setValue(orderList);
    }
}
