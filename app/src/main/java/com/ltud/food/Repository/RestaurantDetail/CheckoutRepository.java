package com.ltud.food.Repository.RestaurantDetail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    //get an order food
    public MutableLiveData<Order> getOrderList(String orderID)
    {
        MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();

        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        Map<String, Object> restaurantMap = (Map<String, Object>) document.get("restaurant");
                        String resID = restaurantMap.get("id").toString();
                        String resName = restaurantMap.get("name").toString();
                        String resAddress = restaurantMap.get("address").toString();
                        String resImg = restaurantMap.get("img").toString();
                        double resRate = (double) restaurantMap.get("rate");
                        Restaurant restaurant = new Restaurant(resID, resName, resAddress, resImg, resRate);

                        List<Map<String, Object>> foodGroup = (List<Map<String, Object>>) document.get("food");
                        List<Order_Food> foodList = new ArrayList<>();
                        for (Map<String, Object> food : foodGroup)
                        {
                            String foodID = food.get("id").toString();
                            String foodName = food.get("name").toString();
                            String foodImg = food.get("img").toString();
                            long price = (long) food.get("price");
                            long rate = (long) food.get("rate");
                            long quantity = (long) food.get("quantity");
                            Order_Food order_food = new Order_Food(foodID, foodName, foodImg, price, rate, quantity);
                            foodList.add(order_food);
                        }

                        String id = document.get("id").toString();
                        Timestamp ts = (Timestamp) document.get("date");
                        Date date = ts.toDate();
                        long status = (long) document.get("status");
                        long payment_method = (long) document.get("payment_method");
                        Order order = new Order(id, date, status, payment_method, restaurant, foodList);

                        orderMutableLiveData.setValue(order);
                    }
                });
        return orderMutableLiveData;
    }

    //update payment method and status
    public void updateOrderPayment(String orderID, long method, String address)
    {
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        map.put("date", date);
        map.put("location", address);
        map.put("payment_method", method);
        map.put("status", 1);
        map.put("checked_notification", false);
        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .update(map);
    }
}
