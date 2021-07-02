package com.ltud.food.ViewModel.Order.DeliveringTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Order.DeliveringTab.OrderDetailRepository;

public class OrderDetailViewModel extends ViewModel {
    private OrderDetailRepository repository = OrderDetailRepository.getInstance();
    private MutableLiveData<Order> orderMutableLiveData;
    private MutableLiveData<String> customerAddress;

    public LiveData<String> getCustomerAddress()
    {
        if(customerAddress == null)
            customerAddress = repository.getAddressCustomer();
        return customerAddress;
    }

    public LiveData<Order> getOrder(String orderID)
    {
        if(orderMutableLiveData == null)
            orderMutableLiveData = repository.getOrderList(orderID);
        return orderMutableLiveData;
    }

    public void updateCompleteOrder(String orderID, boolean isComplete)
    {
        repository.updateCompleteOrder(orderID, isComplete);
    }
}
