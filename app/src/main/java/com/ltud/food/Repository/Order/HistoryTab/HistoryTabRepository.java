package com.ltud.food.Repository.Order.HistoryTab;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class HistoryTabRepository {

    private static HistoryTabRepository repository;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Customer").document(user.getUid()).collection("Order");

    public static HistoryTabRepository getInstance()
    {
        if(repository == null)
        {
            repository = new HistoryTabRepository();
        }
        return repository;
    }

    public MutableLiveData<List<Order>> getHistoryOrderList()
    {
        MutableLiveData<List<Order>> orderListLiveData = new MutableLiveData<>();
        List<Order> orderList = new ArrayList<>();

        collectionReference.whereEqualTo("status", 2)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
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
                                boolean isComplete = (boolean) document.get("complete");
                                Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                                order.setComplete(isComplete);

                                orderList.add(order);
                            }
                            orderListLiveData.setValue(orderList);
                        }
                    }
                });
        return orderListLiveData;
    }

    // query order by date
    public MutableLiveData<List<Order>> getDateOrderFilter(Date date)
    {
        MutableLiveData<List<Order>> mutableLiveData = new MutableLiveData<>();
        List<Order> orderList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        collectionReference.whereEqualTo("status", 2)
                .whereGreaterThan("date", calendar.getTime())
                .whereLessThan("date", date)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
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
                                boolean isComplete = (boolean) document.get("complete");
                                Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                                order.setComplete(isComplete);

                                orderList.add(order);
                            }
                            mutableLiveData.setValue(orderList);
                        }
                    }
                });

        return mutableLiveData;
    }

    // query order by complete status
    public MutableLiveData<List<Order>> getStatusOrderFilter(boolean isComplete)
    {
        MutableLiveData<List<Order>> mutableLiveData = new MutableLiveData<>();
        List<Order> orderList = new ArrayList<>();

        collectionReference.whereEqualTo("status", 2)
                .whereEqualTo("complete", isComplete)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
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
                                boolean isComplete = (boolean) document.get("complete");
                                Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                                order.setComplete(isComplete);

                                orderList.add(order);
                            }
                            mutableLiveData.setValue(orderList);
                        }
                    }
                });

        return mutableLiveData;
    }

    // query order by complete field and date field
    public MutableLiveData<List<Order>> getOrderFilter(boolean isComplete, Date date)
    {
        MutableLiveData<List<Order>> mutableLiveData = new MutableLiveData<>();
        List<Order> orderList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        collectionReference.whereEqualTo("status", 2)
                .whereEqualTo("complete", isComplete)
                .whereGreaterThan("date", calendar.getTime())
                .whereLessThan("date", date)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
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
                                boolean isComplete = (boolean) document.get("complete");
                                Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                                order.setComplete(isComplete);

                                orderList.add(order);
                            }
                            mutableLiveData.setValue(orderList);
                        }
                    }
                });

        return mutableLiveData;
    }

    //remove all history
    public void removeAnOrder(String orderID)
    {
        collectionReference.document(orderID).delete();
    }
}
