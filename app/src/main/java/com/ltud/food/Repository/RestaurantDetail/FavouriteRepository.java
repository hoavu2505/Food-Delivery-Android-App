package com.ltud.food.Repository.RestaurantDetail;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Model.Favourite;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FavouriteRepository {
    private static FavouriteRepository favouriteRepository;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Customer")
            .document(userID).collection("Favourite");

    public static FavouriteRepository getInstance(){
        if (favouriteRepository == null)
        {
            favouriteRepository = new FavouriteRepository();
        }
        return favouriteRepository;
    }

    //get a favourite
    public MutableLiveData<Favourite> getCurrentFavourite(String resID)
    {
        MutableLiveData<Favourite> favouriteMutableLiveData = new MutableLiveData<>();
        collectionReference.whereEqualTo("restaurant.id", resID)
                .whereEqualTo("check_fav", true).get()
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

                                String id = document.get("id").toString();
                                Boolean check_fav = (Boolean) document.get("check_fav");
                                Favourite favourite = new Favourite(id, check_fav, restaurant);

                                favouriteMutableLiveData.setValue(favourite);
                            }
                        }
                    }
                });

        return favouriteMutableLiveData;
    }

    //add a favourite
    public Favourite addOneFavourite(Restaurant restaurant){
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        boolean check_fav = true;

        //restaurant map
        Map<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("id", restaurant.getId());
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("img", restaurant.getImg());
        restaurantMap.put("address", restaurant.getAddress());
        restaurantMap.put("rate", restaurant.getRate());

        //Fav map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("check_fav",check_fav);
        map.put("restaurant", restaurantMap);

        collectionReference.document(id).set(map);

        Favourite favourite = new Favourite(id, check_fav, restaurant);

        return favourite;
    }

    //remove a favourite
    public void deleteOneFavourite(String favouriteID){
//        db.collection("Customer").document(userID)
//                .collection("Favourite").document(favouriteID)
//                .delete();
        collectionReference.document(favouriteID).delete();
    }

}
