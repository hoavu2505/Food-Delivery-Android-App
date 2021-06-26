package com.ltud.food.Repository.Home;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;

import java.util.ArrayList;
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


    //update quantity of food
    public void updateOrderQuantity(String orderID, int quantity)
    {
        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .update("quantity", quantity);
    }

    //remove a order
    public void deleteOneOrder(String orderID)
    {
        db.collection("Customer").document(userID)
                .collection("Order").document(orderID)
                .delete();
    }
}
