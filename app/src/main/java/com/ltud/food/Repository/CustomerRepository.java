package com.ltud.food.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Customer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private static CustomerRepository customerRepository;
    private CollectionReference customerRef = FirebaseFirestore.getInstance().collection("Customer");
    private Customer customer;;

    public static CustomerRepository getInstance() {
        if(customerRepository == null)
        {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }

    public MutableLiveData<Customer> getCustomerLiveData(String customerID)
    {
        MutableLiveData<Customer> customer = new MutableLiveData<>();
        customerRef.document(customerID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        customer.setValue(documentSnapshot.toObject(Customer.class));
                    }
                });

        return customer;
    }

    /*public Customer getCustomer(String customerID)
    {
        customerRef.document(customerID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        customer = documentSnapshot.toObject(Customer.class);
                    }
                });


        return  customer;
    }*/
}
