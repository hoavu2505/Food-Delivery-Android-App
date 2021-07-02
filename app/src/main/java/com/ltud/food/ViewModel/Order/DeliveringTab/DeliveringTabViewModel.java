package com.ltud.food.ViewModel.Order.DeliveringTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.DeliveringTab.DeliveringTabRepository;

import java.util.List;

public class DeliveringTabViewModel extends ViewModel {

    private DeliveringTabRepository repository = DeliveringTabRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getDeliveringOrderList()
    {
        if(orderListLiveData == null)
        {
            orderListLiveData = repository.getDeliveringOrderList();
        }
        return orderListLiveData;
    }
}
