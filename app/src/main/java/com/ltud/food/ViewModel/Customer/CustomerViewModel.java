package com.ltud.food.ViewModel.Customer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Customer;
import com.ltud.food.Repository.Customer.CustomerRepository;

public class CustomerViewModel extends ViewModel {
    private CustomerRepository customerRepository = CustomerRepository.getInstance();

    public LiveData<Customer> getCustomerLiveData(String customerID)
    {
        return customerRepository.getCustomerLiveData(customerID);
    }
}
