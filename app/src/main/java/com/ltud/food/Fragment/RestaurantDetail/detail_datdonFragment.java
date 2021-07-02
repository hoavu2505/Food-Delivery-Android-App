package com.ltud.food.Fragment.RestaurantDetail;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Adapter.FoodAdapter;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.R;
import com.ltud.food.ViewModel.RestaurantDetail.datDonViewModel;
import com.nex3z.notificationbadge.NotificationBadge;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class detail_datdonFragment extends Fragment implements FoodAdapter.AddCallbacks, View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<Food> foodArrayList;
    com.ltud.food.Adapter.FoodAdapter FoodAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    datDonViewModel viewModel;
    String orderID;
    List<String> orderedFoodList = new ArrayList<>();
    NavController navController;

    String res_id;

    private static int cart_count=0;

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
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FoodAdapter foodAdapter = new FoodAdapter();
        foodAdapter.setCallback(this);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(getActivity()).get(datDonViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAddProduct(int pos) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
            return;
        }

        NotificationBadge notificationBadge;
        notificationBadge = getView().findViewById(R.id.badge);

        RelativeLayout ly_cart = getView().findViewById(R.id.ly_cart);
        ly_cart.setVisibility(getView().VISIBLE);
        ImageView imgCart = getView().findViewById(R.id.restaurant_detail_cart);

        //create and update cart in database
        RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
        String resId = restaurantDetailFragment.get_resId();
        String resName = restaurantDetailFragment.getName();
        String resAddress = restaurantDetailFragment.getAddress();
        float resRate = restaurantDetailFragment.getRate();
        String resImg = restaurantDetailFragment.getImg();
        Restaurant restaurant = new Restaurant(resId, resName, resAddress, resImg, resRate);
        Food food = new Food(foodArrayList.get(pos).getId(), foodArrayList.get(pos).getName(), foodArrayList.get(pos).getImg(),
                foodArrayList.get(pos).getPrice(), foodArrayList.get(pos).getRate());
        Log.i("log", String.valueOf(orderedFoodList.size()));

        if(cart_count == 0)
        {
            cart_count++;
            orderID = viewModel.addOneOrder(restaurant, food);
        }
        else{
            for (String id : orderedFoodList)
            {
                if(id.equals(food.getId()))
                {
                    Toast.makeText(getActivity(), "Món ăn đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            viewModel.addFood(orderID, food);
            cart_count++;
        }
        orderedFoodList.add(food.getId());
        notificationBadge.setNumber(cart_count);
        imgCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NavDirections action = RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToCartFragment(orderID);
        navController.navigate(action);
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
        FoodAdapter = new FoodAdapter(getActivity(), foodArrayList,this);

        recyclerView.setAdapter(FoodAdapter);

        //Get id cua restaurant de thuc hien truy van food
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cart_count = 0;
    }


}