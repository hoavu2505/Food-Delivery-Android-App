package com.ltud.food.Repository.RestaurantDetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRepository {
    private static CartRepository cartRepository;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static CartRepository getInstance()
    {
        if(cartRepository == null)
        {
            cartRepository = new CartRepository();
        }
        return cartRepository;
    }

    //get an order
    public MutableLiveData<Order> getCurrentOrder(String orderID)
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

    //update quantity of food
    public void updateFoodQuantity(String orderID, List<Order_Food> foodList)
    {
        List<Map<String, Object>> foodGroup = new ArrayList<>();
        for (Order_Food food : foodList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("id", food.getId());
            map.put("name", food.getName());
            map.put("img", food.getImg());
            map.put("price", food.getPrice());
            map.put("rate", food.getRate());
            map.put("quantity", food.getQuantity());
            foodGroup.add(map);
        }

        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .update("food", foodGroup);
    }

    //remove a food
    public void deleteOneFood(String orderID, Order_Food food)
    {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("img", food.getImg());
        foodMap.put("price", food.getPrice());
        foodMap.put("rate", food.getRate());
        foodMap.put("quantity", food.getQuantity());

        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .update("food", FieldValue.arrayRemove(foodMap));
    }

    //remove a order
    public void deleteOneOrder(String orderID)
    {
        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .delete();
    }
}
