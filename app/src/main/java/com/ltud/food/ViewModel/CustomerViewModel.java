package com.ltud.food.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Customer;
import com.ltud.food.Repository.CustomerRepository;

import java.util.List;

public class CustomerViewModel extends ViewModel {
    private String customerID = "";
    private CustomerRepository customerRepository = CustomerRepository.getInstance();

    public LiveData<Customer> getCustomerLiveData(String customerID)
    {
        return customerRepository.getCustomerLiveData(customerID);
    }

    public String getCustomerID()
    {
        return customerID;
    }

    public void setCustomerID(String id) {
        if(customerID.isEmpty())
        {
            this.customerID = id;
            Log.d("id", id);
        }

    }
}
