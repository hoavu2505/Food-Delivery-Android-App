package com.ltud.food.ViewModel.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Order;
import com.ltud.food.Repository.Home.CheckoutRepository;

import java.util.List;

public class CheckoutViewModel extends ViewModel {

    private final CheckoutRepository repository = CheckoutRepository.getInstance();
    private MutableLiveData<List<Order>> orderListLiveData;
    private MutableLiveData<String> addressCustomerLiveData;

    public LiveData<String> getAddressCustomer()
    {
        if(addressCustomerLiveData == null)
        {
            addressCustomerLiveData = repository.getAddressCustomer();
        }
        return addressCustomerLiveData;
    }

    public LiveData<List<Order>> getOrderList()
    {
        if(orderListLiveData == null)
        {
            orderListLiveData = repository.getOrderListLiveData();
        }
        return orderListLiveData;
    }

    public void updateAddressCustomer(String address)
    {
        repository.updateAddressCustomer(address);
        addressCustomerLiveData.setValue(address);
    }

    public void updateOrderPayment(long method)
    {
        repository.updateOrderPayment(method);
        List<Order> orders = orderListLiveData.getValue();
        for (int i=0; i<orders.size(); i++)
        {
            orders.get(i).setPayment_method(method);
            orders.get(i).setStatus(1);
        }
        orderListLiveData.setValue(orders);
    }
}
