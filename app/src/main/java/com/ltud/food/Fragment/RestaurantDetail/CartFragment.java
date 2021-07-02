package com.ltud.food.Fragment.RestaurantDetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.ltud.food.Adapter.CartListAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.R;
import com.ltud.food.ViewModel.RestaurantDetail.CartViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartFragment extends Fragment implements CartListAdapter.ChangeQuantity, View.OnClickListener {

    private CartListAdapter adapter;
    private CartViewModel cartViewModel;
    private TextView tvTotalPrice;
    private ImageView imvClose;
    private List<Order_Food> orderFoodList;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private String orderID;
    private Restaurant restaurant;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

        tvTotalPrice = (TextView) view.findViewById(R.id.tv_tong_tien);
        imvClose = (ImageView) view.findViewById(R.id.imv_close);
        MaterialButton btnThanhtoan = (MaterialButton) view.findViewById(R.id.btn_thanh_toan);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rec_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CartListAdapter(this);
        recyclerView.setAdapter(adapter);
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();
        orderID = CartFragmentArgs.fromBundle(getArguments()).getOrderID();

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        cartViewModel.getOrderList(orderID).observe(getViewLifecycleOwner(), new Observer<List<Order_Food>>() {
            @Override
            public void onChanged(List<Order_Food> order_foods) {
                adapter.setOrderList(order_foods);
                adapter.notifyDataSetChanged();
                orderFoodList = order_foods;

                double totalPrice = 0;
                for (Order_Food food : order_foods)
                {
                    totalPrice += food.getPrice() * food.getQuantity();
                }
                tvTotalPrice.setText(String.valueOf(totalPrice));

                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        cartViewModel.getRestaurant(orderID).observe(getViewLifecycleOwner(), new Observer<Restaurant>() {
            @Override
            public void onChanged(Restaurant res) {
                restaurant = res;
            }
        });

        btnThanhtoan.setOnClickListener(this);
        imvClose.setOnClickListener(this);
    }

    @Override
    public void onIncreaseQuantity(int index) {
        long quantity = orderFoodList.get(index).getQuantity() + 1;
        orderFoodList.get(index).setQuantity(quantity);
        cartViewModel.updateFoodQuantity(orderID, orderFoodList);
    }

    @Override
    public void onDecreaseQuantity(int index) {
        long quantity = orderFoodList.get(index).getQuantity() - 1;
        if(quantity == 0)
        {
            if (orderFoodList.size() == 1)
            {
                cartViewModel.deleteOneOrder(orderID);
            }
            else
                cartViewModel.deleteOneFood(orderID, index, orderFoodList.get(index));
        }
        else
        {
            orderFoodList.get(index).setQuantity(quantity);
            cartViewModel.updateFoodQuantity(orderID, orderFoodList);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_thanh_toan)
        {
            NavDirections action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(orderID);
            navController.navigate(action);
        }
        else {
            NavDirections action = CartFragmentDirections.actionCartFragmentToRestaurantDetailFragment(
                    restaurant.name, restaurant.address, restaurant.id, restaurant.img, (float) restaurant.rate
            );
            navController.navigate(action);
        }
    }
}