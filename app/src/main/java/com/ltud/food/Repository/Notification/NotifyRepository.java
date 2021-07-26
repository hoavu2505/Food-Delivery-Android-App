package com.ltud.food.Repository.Notification;

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
import com.ltud.food.Repository.Order.HistoryTab.HistoryTabRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotifyRepository {

    private static NotifyRepository repository;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Customer").document(user.getUid()).collection("Order");

    public static NotifyRepository getInstance()
    {
        if(repository == null)
        {
            repository = new NotifyRepository();
        }
        return repository;
    }

    public MutableLiveData<List<Order>> getOrderList()
    {
        MutableLiveData<List<Order>> orderListLiveData = new MutableLiveData<>();
        List<Order> orderList = new ArrayList<>();

        collectionReference.whereNotEqualTo("status", 0)
                .whereEqualTo("checked_notification", false)
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

                                Order order = new Order(id, date, status, payment_method, restaurant, foodList);
                                if(document.get("complete") != null){
                                    boolean isComplete = (boolean) document.get("complete");
                                    order.setComplete(isComplete);
                                }
                                order.setLocation(document.get("location").toString());
                                order.setChecked_notify((boolean) document.get("checked_notification"));

                                orderList.add(order);
                            }
                            orderListLiveData.setValue(orderList);
                        }
                    }
                });
        return orderListLiveData;
    }

    public void updateCheckedNotify(String orderID)
    {
        Date date = new Date();
        collectionReference.document(orderID)
                .update("checked_notification", true,
                "date", date);
    }

}
