package com.ltud.food.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Customer;
import com.ltud.food.Repository.CustomerRepository;

import java.util.List;

public class CustomerViewModel extends ViewModel {
    private MutableLiveData<Customer> customer;
    private CustomerRepository customerRepository = CustomerRepository.getInstance();

    public LiveData<Customer> getCustomerLiveData(String customerID)
    {
        return customerRepository.getCustomerLiveData(customerID);
    }

    /*public Customer getCustomer(String customerID)
    {
        return customerRepository.getCustomer(customerID);
    }*/
}
