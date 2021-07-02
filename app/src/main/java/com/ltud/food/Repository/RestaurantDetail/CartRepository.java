package com.ltud.food.Repository.RestaurantDetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
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

    //get ordered food list
    public MutableLiveData<List<Order_Food>> getOrderListLiveData(String orderID)
    {
        MutableLiveData<List<Order_Food>> foodListLiveData = new MutableLiveData<>();
        List<Order_Food> foodList = new ArrayList<>();

        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<Map<String, Object>> foodGroup = (List<Map<String, Object>>) documentSnapshot.get("food");
                        for (Map<String, Object> food : foodGroup)
                        {
                            String id = food.get("id").toString();
                            String name = food.get("name").toString();
                            String img = food.get("img").toString();
                            double price = (double) food.get("price");
                            long rate = (long) food.get("rate");
                            long quantity = (long) food.get("quantity");
                            foodList.add(new Order_Food(id, name, img, price, rate, quantity));
                        }
                        foodListLiveData.setValue(foodList);
                    }
                });
        return foodListLiveData;
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

    public MutableLiveData<Restaurant> getRestaurant(String orderID)
    {
        MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();

        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = (Map<String, Object>) documentSnapshot.get("restaurant");
                        String id = map.get("id").toString();
                        String name = map.get("name").toString();
                        String img = map.get("img").toString();
                        String address = map.get("address").toString();
                        double rate = (double) map.get("rate");
                        Restaurant restaurant = new Restaurant(id, name, address, img, rate);

                        restaurantMutableLiveData.setValue(restaurant);
                    }
                });
        return restaurantMutableLiveData;
    }


}
