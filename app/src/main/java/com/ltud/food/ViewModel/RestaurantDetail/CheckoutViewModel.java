package com.ltud.food.ViewModel.RestaurantDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Repository.RestaurantDetail.CheckoutRepository;

import java.util.List;

public class CheckoutViewModel extends ViewModel {

    private final CheckoutRepository repository = CheckoutRepository.getInstance();
    private MutableLiveData<Order> orderLiveData;
    private MutableLiveData<String> addressCustomerLiveData;

    public LiveData<String> getAddressCustomer()
    {
        if(addressCustomerLiveData == null)
        {
            addressCustomerLiveData = repository.getAddressCustomer();
        }
        return addressCustomerLiveData;
    }

    public LiveData<Order> getOrder(String orderID)
    {
        if(orderLiveData == null)
        {
            orderLiveData = repository.getOrderList(orderID);
        }
        return orderLiveData;
    }

    public void updateAddressCustomer(String address)
    {
        repository.updateAddressCustomer(address);
        addressCustomerLiveData.setValue(address);
    }

    public void updateOrderPayment(String orderID, long method)
    {
        repository.updateOrderPayment(orderID, method);
        Order order = orderLiveData.getValue();
        order.setPayment_method(method);
        orderLiveData.setValue(order);
    }
}
