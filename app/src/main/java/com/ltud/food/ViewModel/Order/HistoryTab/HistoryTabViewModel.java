package com.ltud.food.ViewModel.Order.HistoryTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.HistoryTab.HistoryTabRepository;

import java.util.List;

public class HistoryTabViewModel extends ViewModel {

    private HistoryTabRepository repository = HistoryTabRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getHistoryOrderList()
    {
        if(orderListLiveData == null)
        {
            orderListLiveData = repository.getHistoryOrderList();
        }
        return orderListLiveData;
    }
}
