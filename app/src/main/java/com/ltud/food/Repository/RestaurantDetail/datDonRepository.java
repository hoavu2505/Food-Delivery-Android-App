package com.ltud.food.Repository.RestaurantDetail;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // add a order
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String addOneOrder(Restaurant restaurant, Food food)
    {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        DateFormat today = new SimpleDateFormat("dd/MM/yyyy");
        String date = today.format(new Date());
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
        foodMap.put("quantity", 1);

        //list of food map
        List<Map<String, Object>> parentFoodMap = new ArrayList<>();
        parentFoodMap.add(foodMap);

        //order map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("date", date);
        map.put("status", status);
        map.put("payment_method", payment_method);
        map.put("restaurant", restaurantMap);
        map.put("food", parentFoodMap);
        colRef.document(id).set(map);

        return id;
    }

    //add food
    public void addFood(String orderID, Food food)
    {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("img", food.getImg());
        foodMap.put("price", food.getPrice());
        foodMap.put("rate", food.getRate());
        foodMap.put("quantity", 1);

        colRef.document(orderID).update("food", FieldValue.arrayUnion(foodMap));
    }
}
