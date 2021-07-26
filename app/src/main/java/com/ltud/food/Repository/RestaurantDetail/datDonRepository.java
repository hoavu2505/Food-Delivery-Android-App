package com.ltud.food.Repository.RestaurantDetail;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class datDonRepository {

    private static datDonRepository repository;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final CollectionReference colRef = FirebaseFirestore.getInstance().collection("Customer")
            .document(userID).collection("Order");

    public static datDonRepository getInstance()
    {
        if(repository == null)
        {
            repository = new datDonRepository();
        }
        return repository;
    }

    // get current order
    public MutableLiveData<Order> getCurrentOrder(String resID)
    {
        MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();

        colRef.whereEqualTo("restaurant.id", resID)
                .whereEqualTo("status", 0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for(DocumentSnapshot document : queryDocumentSnapshots)
                            {
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
                        }
                    }
                });
        return orderMutableLiveData;
    }

    // add an order
    public Order addOneOrder(Restaurant restaurant, Order_Food food)
    {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        int status = 0;
        int payment_method = 0;

        //restaurant map
        Map<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("id", restaurant.getId());
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("img", restaurant.getImg());
        restaurantMap.put("address", restaurant.getAddress());
        restaurantMap.put("rate", restaurant.getRate());

        //food map
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("img", food.getImg());
        foodMap.put("price", food.getPrice());
        foodMap.put("rate", food.getRate());
        foodMap.put("quantity", food.getQuantity());

        //list of food map
        List<Map<String, Object>> parentFoodMap = new ArrayList<>();
        parentFoodMap.add(foodMap);

        //order map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("date", calendar.getTime());
        map.put("status", status);
        map.put("payment_method", payment_method);
        map.put("restaurant", restaurantMap);
        map.put("food", parentFoodMap);
        colRef.document(id).set(map);

        List<Order_Food> foodList = new ArrayList<>();
        foodList.add(food);
        Order order = new Order(id, calendar.getTime(), status, payment_method,restaurant, foodList);

        return order;
    }

    //add food
    public void addFood(String orderID, Order_Food food)
    {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("img", food.getImg());
        foodMap.put("price", food.getPrice());
        foodMap.put("rate", food.getRate());
        foodMap.put("quantity", food.getQuantity());

        colRef.document(orderID).update("food", FieldValue.arrayUnion(foodMap));
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

        colRef.document(orderID).update("food", foodGroup);
    }
}
