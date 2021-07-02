package com.ltud.food.Repository.Order.HistoryTab;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.Order.DeliveringTab.OrderDetailRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryDetailRepository {

    private static HistoryDetailRepository repository;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static HistoryDetailRepository getInstance()
    {
        if(repository == null)
        {
            repository = new HistoryDetailRepository();
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

    //get an order food
    public MutableLiveData<Order> getSelectedOrder(String orderID)
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
                            double price = (double) food.get("price");
                            long rate = (long) food.get("rate");
                            long quantity = (long) food.get("quantity");
                            Order_Food order_food = new Order_Food(foodID, foodName, foodImg, price, rate, quantity);
                            foodList.add(order_food);
                        }

                        String id = document.get("id").toString();
                        String date = document.get("date").toString();
                        long status = (long) document.get("status");
                        long payment_method = (long) document.get("payment_method");
                        boolean isComplete = (boolean)  document.get("complete");
                        Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                        order.setComplete(isComplete);

                        orderMutableLiveData.setValue(order);
                    }
                });
        return orderMutableLiveData;
    }


}
