package com.ltud.food.Repository.Order.HistoryTab;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.Order.DeliveringTab.OrderDetailRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HistoryDetailRepository {

    private static HistoryDetailRepository repository;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static HistoryDetailRepository getInstance()
    {
        repository = new HistoryDetailRepository();
        return repository;
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
                        String location = document.get("location").toString();
                        boolean isComplete = (boolean)  document.get("complete");
                        Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                        order.setLocation(location);
                        order.setComplete(isComplete);

                        orderMutableLiveData.setValue(order);
                    }
                });
        return orderMutableLiveData;
    }

    public LiveData<String> getOrderID(String resID)
    {
        MutableLiveData<String> orderID = new MutableLiveData<>();
        db.collection("Customer").document(userID).collection("Order")
                .whereEqualTo("restaurant.id", resID)
                .whereEqualTo("status", 0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                            {
                                orderID.setValue(documentSnapshot.get("id").toString());
                            }
                        }
                    }
                });
        return orderID;
    }

    public void removeAnOrder(String orderID)
    {
        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .delete();
    }

    // add an order
    public void createNewOrder(Restaurant restaurant, List<Order_Food> foods)
    {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        Date date = new Date();
        int status = 0;
        int payment_method = 0;

        //restaurant map
        Map<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("id", restaurant.getId());
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("img", restaurant.getImg());
        restaurantMap.put("address", restaurant.getAddress());
        restaurantMap.put("rate", restaurant.getRate());

        //list of food map
        List<Map<String, Object>> parentFoodMap = new ArrayList<>();
        for(Order_Food food : foods)
        {
            //food map
            Map<String, Object> foodMap = new HashMap<>();
            foodMap.put("id", food.getId());
            foodMap.put("name", food.getName());
            foodMap.put("img", food.getImg());
            foodMap.put("price", food.getPrice());
            foodMap.put("rate", food.getRate());
            foodMap.put("quantity", food.getQuantity());

            parentFoodMap.add(foodMap);
        }

        //order map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("date", date);
        map.put("status", status);
        map.put("payment_method", payment_method);
        map.put("restaurant", restaurantMap);
        map.put("food", parentFoodMap);
        db.collection("Customer").document(userID)
                .collection("Order").document(id)
                .set(map);
    }
}
