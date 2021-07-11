package com.ltud.food.Fragment.Home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Adapter.RestaurantAdapter;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.R;

import java.util.ArrayList;

public class gantoiFragment extends Fragment{

    RecyclerView recyclerView;
    ArrayList<Restaurant> restaurantArrayList;
    com.ltud.food.Adapter.RestaurantAdapter RestaurantAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public gantoiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gantoi, container, false);
        // Inflate the layout for this fragment



        return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //Su dung Dialog de thong bao load du lieu
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang lấy dữ liệu...");
        progressDialog.show();

        recyclerView = (RecyclerView) getView().findViewById(R.id.gantoi_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        db = FirebaseFirestore.getInstance();

        restaurantArrayList = new ArrayList<Restaurant>();
        RestaurantAdapter = new RestaurantAdapter(getActivity(),restaurantArrayList);

        recyclerView.setAdapter(RestaurantAdapter);


        EventChangeListener();

    }



    //Ham truy van
    private void EventChangeListener() {
        db.collection("Restaurants").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                restaurantArrayList.add(dc.getDocument().toObject(Restaurant.class));
                            }

                            if (dc.getType() == DocumentChange.Type.REMOVED){

                            }

                            RestaurantAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });
    }

}