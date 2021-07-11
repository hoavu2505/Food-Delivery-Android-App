package com.ltud.food.ViewModel.Order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.DeliveringTab.DeliveringTabRepository;
import com.ltud.food.Repository.Order.DraftTabRepository;

import java.util.List;

public class DraftTabViewModel extends ViewModel {

    private DraftTabRepository repository = DraftTabRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;

    public LiveData<List<Order>> getDraftOrder()
    {
        orderListLiveData = repository.getDraftOrder();
        return orderListLiveData;
    }

    public void removeDraftOrder(String orderID, int position)
    {
        repository.removeDraftOrder(orderID);
        List<Order> orderList = orderListLiveData.getValue();
        orderList.remove(position);
        orderListLiveData.setValue(orderList);
    }


}
