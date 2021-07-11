package com.ltud.food.Fragment.Order.DeliveringTab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.DeliveringTab.OrderDetailViewModel;

import org.jetbrains.annotations.NotNull;

public class deliveringDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack, imvPhone, imvChat, imvLocation;
    private TextView tvResName, tvOrderID, tvAddress, tvDate, tvPrice, tvTotalPrice;
    private RecyclerView recyclerView;
    private MaterialButton btnReceived, btnCancel;
    private CheckoutAdapter adapter;
    private CustomProgressDialog progressDialog;
    private OrderDetailViewModel viewModel;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private String orderID;
    private Order currentOrder;

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

        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

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
                tvDate.setText(order.getDate());

                long price = 0;
                for (Order_Food food : order.getFoodList())
                {
                    price += food.getPrice() * food.getQuantity();
                }
                tvPrice.setText(String.format("%sđ", String.valueOf(price)));
                tvTotalPrice.setText(String.format("%sđ", String.valueOf(price + 15000)));

                currentOrder = order;
            }
        });
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        bottomNavigationView.setVisibility(View.VISIBLE);
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
        showSuccessNotification();
    }

    private void canceledOrder() {
        viewModel.updateCompleteOrder(orderID, false);
        navController.navigate(R.id.orderFragment);
        showFailureNotification();
    }

    private void showSuccessNotification() {
        final String CHANNEL_ID = "1";
        String title = "Đơn hàng đã được giao";
        String message = String.format("Đơn hàng đã được giao đến địa chỉ %s. Cảm ơn bạn đã sử dụng dịch vụ Eat Now. " +
                "Hãy chia sẻ cảm nhận của bạn và tiếp tục đặt hàng nhé !", currentOrder.getLocation());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.app_image)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(1, builder.build());
    }

    private void showFailureNotification() {
        final String CHANNEL_ID = "1";
        String title = "Đã hủy đơn hàng";
        String message = "Cảm ơn bạn đã sử dụng dịch vụ Eat Now. Hãy ấn đặt lại đơn hàng bất cứ khi nào bạn muốn !";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.app_image)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(2, builder.build());
    }

    private void createNotificationChannel()
    {
        final String CHANNEL_ID = "1";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channel_name = getResources().getString(R.string.app_name);
            String description = "Food app e-commerce";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}