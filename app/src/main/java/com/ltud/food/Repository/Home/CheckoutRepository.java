package com.ltud.food.Repository.Home;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutRepository {

    private static CheckoutRepository repository;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static CheckoutRepository getInstance()
    {
        if(repository == null)
        {
            repository = new CheckoutRepository();
        }
        return repository;
    }

    // get address
    public MutableLiveData<String> getAddressCustomer()
    {
        MutableLiveData<String> address = new MutableLiveData<>();

        db.collection("Customer").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        address.setValue(documentSnapshot.get("address").toString());
                    }
                });

        return address;
    }

    //update address
    public void updateAddressCustomer(String address)
    {
        db.collection("Customer").document(userID)
                .update("address", address);
    }

    //get order list
    public MutableLiveData<List<Order>> getOrderListLiveData()
    {
        MutableLiveData<List<Order>> orderList = new MutableLiveData<>();
        List<Order> list = new ArrayList<>();

        db.collection("Customer").document(userID).collection("Order")
                .whereEqualTo("status", 0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                            {
                                Order order = new Order();
                                order.setId(document.get("id").toString());
                                order.setDate(document.get("date").toString());
                                order.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                                order.setStatus((long) document.get("status"));
                                order.setPayment_method((long) document.get("payment_method"));
                                Map<String, Object> group = (Map<String, Object>) document.get("food");
                                Food food = new Food(group.get("id").toString(), group.get("name").toString(), group.get("img").toString(),
                                        (long) group.get("price"), (long) group.get("rate"));
                                order.setFood(food);
                                list.add(order);
                            }
                            orderList.setValue(list);
                        }
                    }
                });
        return orderList;
    }

    //update payment method and status
    public void updateOrderPayment(long method)
    {
        db.collection("Customer").document(userID).collection("Order")
                .whereEqualTo("status", 0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                            {
                                db.collection("Customer").document(userID)
                                        .collection("Order").document(document.get("id").toString())
                                        .update("payment_method", method,
                                                "status", 1);
                            }
                        }
                    }
                });
    }
}
