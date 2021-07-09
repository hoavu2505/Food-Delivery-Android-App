package com.ltud.food.ViewModel.Notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Notification.NotifyRepository;
import com.ltud.food.Repository.Order.HistoryTab.HistoryTabRepository;

import java.util.List;

public class NotifyViewModel extends ViewModel {

    private NotifyRepository repository = NotifyRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getOrderList()
    {
        orderListLiveData = repository.getOrderList();
        return orderListLiveData;
    }
}
