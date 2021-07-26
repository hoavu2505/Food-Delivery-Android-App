package com.ltud.food.Fragment.RestaurantDetail;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.widget.Toast;

import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.RestaurantDetail.CheckoutViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CheckoutFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack, imvEditAddress;
    private TextView tvLocation, tvDate, tvPrice, tvTotalPrice;
    private Button btnCheckout;
    private RadioButton rdbDigitalWallet, rdbCash;
    private RecyclerView recyclerView;
    private CheckoutAdapter adapter;
    private CheckoutViewModel checkoutViewModel;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private String orderID;
    private String address;
    private Order currentOrder;

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

        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();

        imvBack = view.findViewById(R.id.imv_back);
        imvEditAddress = view.findViewById(R.id.imv_edit_address);
        tvLocation = view.findViewById(R.id.tv_address);
        tvDate = view.findViewById(R.id.tv_date);
        tvPrice = view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = view.findViewById(R.id.tv_tong_tien);
        rdbDigitalWallet = view.findViewById(R.id.rdb_digital_wallet);
        rdbCash = view.findViewById(R.id.rdb_cash);
        btnCheckout = view.findViewById(R.id.btn_thanh_toan);
        recyclerView = view.findViewById(R.id.rec_order_list);
        navController = Navigation.findNavController(view);
        orderID = CheckoutFragmentArgs.fromBundle(getArguments()).getOrderID();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        checkoutViewModel = new ViewModelProvider(getActivity()).get(CheckoutViewModel.class);
        liveDataObserve();

        imvBack.setOnClickListener(this);
        imvEditAddress.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
    }

    private void liveDataObserve() {

        checkoutViewModel.getAddressCustomer().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvLocation.setText(s);
                address = s;
            }
        });

        checkoutViewModel.getOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                adapter.setOrderList(order.getFoodList());
                adapter.notifyDataSetChanged();
                currentOrder = order;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvDate.setText(dateFormat.format(order.getDate()));
                if(order.getPayment_method() == 0)
                    rdbDigitalWallet.setChecked(true);
                else
                    rdbCash.setChecked(true);

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
            case R.id.imv_edit_address: changeLocation(); break;
            case R.id.btn_thanh_toan: orderCheckOut(); break;
        }
    }

    private void backToCart() {
        NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToCartFragment(orderID);
        navController.navigate(action);
    }

    private void changeLocation() {
        NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToAutoCompleteLocationFragment()
                .setNavigatePayment(true)
                .setOrderID(orderID);
        navController.navigate(action);
    }

    private void orderCheckOut() {
        if(address.isEmpty())
        {
            Toast.makeText(getActivity(), "Thêm địa chỉ giao hàng", Toast.LENGTH_LONG).show();
            return;
        }
        long method = 0;
        if(rdbCash.isChecked())
            method = 1;
        checkoutViewModel.updateOrderPayment(orderID, method, address);
        navController.navigate(R.id.orderFragment);

        showNotification();
    }

    private void showNotification() {
        final String CHANNEL_ID = "123";
        String title = "Đặt hàng thành công";
        String message = String.format("Đơn hàng tại %s - %s được đặt thành công và sẽ được giao bởi Eat Now",
                    currentOrder.getRestaurant().getName(), currentOrder.getRestaurant().getAddress());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.splash_logo)
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

    private void createNotificationChannel()
    {
        final String CHANNEL_ID = "123";
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