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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.RestaurantDetail.CheckoutViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckoutFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack;
    private TextView tvLocation, tvDate, tvPrice, tvTotalPrice;
    private Button btnCheckout;
    private RadioButton rdbDigitalWallet, rdbCash;
    private RecyclerView recyclerView;
    private CheckoutAdapter adapter;
    private CheckoutViewModel checkoutViewModel;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private String orderID;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

        imvBack = view.findViewById(R.id.imv_back);
        tvLocation = view.findViewById(R.id.tv_address);
        tvDate = view.findViewById(R.id.tv_date);
        tvPrice = view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = view.findViewById(R.id.tv_tong_tien);
        rdbDigitalWallet = view.findViewById(R.id.rdb_digital_wallet);
        rdbCash = view.findViewById(R.id.rdb_cash);
        btnCheckout = view.findViewById(R.id.btn_thanh_toan);
        recyclerView = view.findViewById(R.id.rec_order_list);
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();
        orderID = CheckoutFragmentArgs.fromBundle(getArguments()).getOrderID();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        checkoutViewModel = new ViewModelProvider(getActivity()).get(CheckoutViewModel.class);
        liveDataObserve();

        imvBack.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
    }

    private void liveDataObserve() {

        checkoutViewModel.getAddressCustomer().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvLocation.setText(s);
            }
        });

        checkoutViewModel.getOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                adapter.setOrderList(order.getFoodList());
                adapter.notifyDataSetChanged();

                tvDate.setText(order.getDate());
                if(order.getPayment_method() == 0)
                    rdbDigitalWallet.setChecked(true);
                else
                    rdbCash.setChecked(true);

                double price = 0;
                for (Order_Food food : order.getFoodList())
                {
                    price += food.getPrice() * food.getQuantity();
                }
                tvPrice.setText(String.format("%sđ", String.valueOf(price)));
                tvTotalPrice.setText(String.format("%sđ", String.valueOf(price + 15000)));
            }
        });
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();

        String address = CheckoutFragmentArgs.fromBundle(getArguments()).getAddress();
        if(!address.equals("null"))
            checkoutViewModel.updateAddressCustomer(address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imv_back: backToCart(); break;
            case R.id.tv_address: changeLocation(); break;
            case R.id.btn_thanh_toan: orderCheckOut(); break;
        }
    }

    private void backToCart() {
        NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToCartFragment(orderID);
        navController.navigate(action);
    }

    private void changeLocation() {
        NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToAutoCompleteLocationFragment()
                .setNavigatePayment(true);
        navController.navigate(action);
    }

    private void orderCheckOut() {
        long method = 0;
        if(rdbCash.isChecked())
            method = 1;
        checkoutViewModel.updateOrderPayment(orderID, method);
        navController.navigate(R.id.orderFragment);
    }

}