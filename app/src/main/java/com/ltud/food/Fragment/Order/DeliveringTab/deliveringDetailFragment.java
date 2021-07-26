package com.ltud.food.Fragment.Order.DeliveringTab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.DeliveringTab.OrderDetailViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class deliveringDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack, imvPhone, imvChat, imvLocation;
    private TextView tvResName, tvOrderID, tvAddress, tvDate, tvPrice, tvTotalPrice;
    private RecyclerView recyclerView;
    private MaterialButton btnReceived, btnCancel;
    private CheckoutAdapter adapter;
    private CustomProgressDialog progressDialog;
    private OrderDetailViewModel viewModel;
    private NavController navController;
    private String orderID;

    public deliveringDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();
        navController = Navigation.findNavController(view);
        orderID = deliveringDetailFragmentArgs.fromBundle(getArguments()).getOrderID();

        imvBack = view.findViewById(R.id.imv_back);
        imvPhone = view.findViewById(R.id.imgPhone);
        imvChat = view.findViewById(R.id.imgChat);
        imvLocation = view.findViewById(R.id.imgLocation);
        tvResName = view.findViewById(R.id.tv_ten_nha_hang);
        tvOrderID = view.findViewById(R.id.tv_ma_don_hang);
        tvAddress = view.findViewById(R.id.tv_address);
        tvDate = view.findViewById(R.id.tv_date);
        tvPrice = view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = view.findViewById(R.id.tv_tong_tien);
        btnReceived = view.findViewById(R.id.btn_da_nhan);
        btnCancel= view.findViewById(R.id.btn_huy);

        viewModel = new ViewModelProvider(getActivity()).get(OrderDetailViewModel.class);
        recyclerView = view.findViewById(R.id.rec_order_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter();
        recyclerView.setAdapter(adapter);

        updateUI();

        imvBack.setOnClickListener(this);
        imvPhone.setOnClickListener(this);
        imvChat.setOnClickListener(this);
        imvLocation.setOnClickListener(this);
        btnReceived.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void updateUI() {
        viewModel.getOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                adapter.setOrderList(order.getFoodList());
                adapter.notifyDataSetChanged();

                tvResName.setText(order.getRestaurant().getAddress());
                tvOrderID.setText(order.getId());
                tvAddress.setText(order.getLocation());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvDate.setText(dateFormat.format(order.getDate()));

                long price = 0;
                for (Order_Food food : order.getFoodList())
                {
                    price += food.getPrice() * food.getQuantity();
                }
                Locale vietnam = new Locale("vi", "VN");
                NumberFormat dongFormat = NumberFormat.getCurrencyInstance(vietnam);
                tvPrice.setText(dongFormat.format(price));
                tvTotalPrice.setText(dongFormat.format(price + 15000));

                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imv_back: navController.navigate(R.id.orderFragment); break;
            case R.id.imgPhone: phoneDialAction(); break;
            case R.id.imgChat: sendSmsAction(); break;
            case R.id.btn_da_nhan: completedOrder(); break;
            case R.id.btn_huy : canceledOrder(); break;
        }
    }

    private void phoneDialAction() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789"));
        startActivity(intent);
    }

    private void sendSmsAction() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:123456789"));
        startActivity(intent);
    }

    private void completedOrder() {
        viewModel.updateCompleteOrder(orderID, true);
        navController.navigate(R.id.orderFragment);
    }

    private void canceledOrder() {
        viewModel.updateCompleteOrder(orderID, false);
        navController.navigate(R.id.orderFragment);
    }
}