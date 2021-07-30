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
import androidx.lifecycle.Observer;
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
import com.ltud.food.Model.Order;
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
    List<Order_Food> orderFoodList = new ArrayList<>();
    Restaurant restaurant;
    com.ltud.food.Adapter.FoodAdapter FoodAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    datDonViewModel viewModel;
    Order currentOrder = new Order();
    NavController navController;
    NotificationBadge notificationBadge;
    RelativeLayout ly_cart;
    ImageView imgCart;

    String res_id, resName, resAddress, resImg, orderID;
    double resRate;

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
        notificationBadge = view.findViewById(R.id.badge);
        ly_cart = view.findViewById(R.id.ly_cart);
        imgCart  = view.findViewById(R.id.restaurant_detail_cart);

        RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
        orderID = restaurantDetailFragment.getOrderID();
        res_id = restaurantDetailFragment.get_resId();
        resName = restaurantDetailFragment.getName();
        resAddress = restaurantDetailFragment.getAddress();
        resRate = restaurantDetailFragment.getRate();
        resImg = restaurantDetailFragment.getImg();
        restaurant = new Restaurant(res_id, resName, resAddress, resImg, resRate);

        imgCart.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            viewModel = new ViewModelProvider(getActivity()).get(datDonViewModel.class);
            viewModel.getCurrentOrder(res_id).observe(getViewLifecycleOwner(), new Observer<Order>() {
                @Override
                public void onChanged(Order order) {
                    int quantity = 0;
                    for(Order_Food food : order.getFoodList())
                    {
                        quantity += food.getQuantity();
                    }
                    cart_count = quantity;
                    notificationBadge.setNumber(cart_count);
                    ly_cart.setVisibility(getView().VISIBLE);
                    currentOrder = order;
                }
            });
        }
    }

    @Override
    public void onAddProduct(int pos) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
            return;
        }

        //create and update cart in database
        String foodID = foodArrayList.get(pos).getId();
        String foodName = foodArrayList.get(pos).getName();
        String foodImg = foodArrayList.get(pos).getImg();
        long foodPrice = foodArrayList.get(pos).getPrice();
        long foodRate = foodArrayList.get(pos).getRate();
        Order_Food food = new Order_Food(foodID, foodName, foodImg, foodPrice, foodRate, 1);

       if(cart_count == 0)
        {
            viewModel.addOneOrder(restaurant, food);
        }
        else{
            for (int i=0; i<currentOrder.getFoodList().size(); i++)
            {
                if(currentOrder.getFoodList().get(i).getId().equals(food.getId()))
                {
                    long quantity = currentOrder.getFoodList().get(i).getQuantity() + 1;
                    currentOrder.getFoodList().get(i).setQuantity(quantity);
                    viewModel.updateFoodQuantity(currentOrder.getId(), currentOrder.getFoodList());
                    return;
                }
            }
            viewModel.addFood(currentOrder.getId(), food);
        }
    }

    @Override
    public void onClick(View v) {
        NavDirections action = RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToCartFragment(currentOrder.getId());
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