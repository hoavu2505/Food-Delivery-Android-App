package com.ltud.food.ViewModel.Order.HistoryTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.HistoryTab.HistoryDetailRepository;

public class HistoryDetailViewModel extends ViewModel {

    private HistoryDetailRepository repository = HistoryDetailRepository.getInstance();
    private MutableLiveData<Order> orderMutableLiveData;

    public LiveData<Order> getSelectedOrder(String orderID)
    {
        if(orderMutableLiveData == null)
        {
            orderMutableLiveData = repository.getSelectedOrder(orderID);
        }
        return orderMutableLiveData;
    }
}
