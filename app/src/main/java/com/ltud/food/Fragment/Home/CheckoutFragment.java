package com.ltud.food.Fragment.Home;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Home.CheckoutViewModel;

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

        imvBack = (ImageView) view.findViewById(R.id.imv_back);
        tvLocation = (TextView) view.findViewById(R.id.tv_address);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvPrice = (TextView) view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = (TextView) view.findViewById(R.id.tv_tong_tien);
        rdbDigitalWallet = (RadioButton) view.findViewById(R.id.rdb_digital_wallet);
        rdbCash = (RadioButton) view.findViewById(R.id.rdb_cash);
        btnCheckout = (Button) view.findViewById(R.id.btn_thanh_toan);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_order_list);
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();

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

        checkoutViewModel.getOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                adapter.setOrderList(orders);
                adapter.notifyDataSetChanged();

                tvDate.setText(orders.get(0).getDate());

                if(orders.get(0).getPayment_method() == 0)
                    rdbDigitalWallet.setChecked(true);
                else rdbCash.setChecked(true);

                double price = 0;
                for (Order order : orders)
                {
                    price += order.getFood().getPrice() * order.getQuantity();
                }
                tvPrice.setText(String.format("%sđ", String.valueOf(price)));
                tvTotalPrice.setText(String.format("%sđ", String.valueOf(price + 15000)));

                progressDialog.dismiss();
            }
        });
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
            case R.id.imv_back: navController.navigate(R.id.cartFragment); break;
            case R.id.tv_address: changeLocation(); break;
            case R.id.btn_thanh_toan: orderCheckOut(); break;
        }
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
        checkoutViewModel.updateOrderPayment(method);
        navController.navigate(R.id.orderFragment);
    }

}