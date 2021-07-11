package com.ltud.food.Repository.Customer;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ltud.food.Model.Customer;

public class CustomerRepository {

    private static CustomerRepository customerRepository;
    private CollectionReference customerRef = FirebaseFirestore.getInstance().collection("Customer");

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
}
