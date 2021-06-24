package com.ltud.food.Fragment.restaurantDetail.detailTabLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Fragment.restaurantDetail.RestaurantDetailFragment;
import com.ltud.food.Model.Food;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class detail_datdonFragment extends Fragment{

    RecyclerView recyclerView;
    ArrayList<Food> foodArrayList;
    FoodAdapter FoodAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    String res_id;


    public detail_datdonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_datdon, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        if (FoodAdapter == null){   //FoodAdapter null thi khong dung progressDialog
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Đang lấy dữ liệu...");
            progressDialog.show();
        }



        recyclerView = (RecyclerView) getView().findViewById(R.id.datdon_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();

        foodArrayList = new ArrayList<Food>();
        FoodAdapter = new FoodAdapter(getActivity(), foodArrayList);

        recyclerView.setAdapter(FoodAdapter);

        RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
        res_id = restaurantDetailFragment.get_resId();


        EventChangeListener(res_id);

    }

    private void EventChangeListener(String id) {
        db.collection("Restaurants").document(id).collection("Food").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                foodArrayList.add(dc.getDocument().toObject(Food.class));
                            }
                            FoodAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }
}