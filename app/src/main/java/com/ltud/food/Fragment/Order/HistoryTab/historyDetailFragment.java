package com.ltud.food.Fragment.Order.HistoryTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.Order.DeliveringTab.deliveringDetailFragmentArgs;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.DeliveringTab.OrderDetailViewModel;
import com.ltud.food.ViewModel.Order.HistoryTab.HistoryDetailViewModel;

import org.jetbrains.annotations.NotNull;

public class historyDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack;
    private TextView tvResName, tvOrderID, tvStatus, tvAddress, tvDate, tvPrice, tvTotalPrice;
    private RecyclerView recyclerView;
    private MaterialButton btnReorder;
    private CheckoutAdapter adapter;
    private CustomProgressDialog progressDialog;
    private HistoryDetailViewModel viewModel;
    private NavController navController;
    private String orderID;

    public historyDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();
        navController = Navigation.findNavController(view);
        orderID = historyDetailFragmentArgs.fromBundle(getArguments()).getOrderID();

        imvBack = view.findViewById(R.id.imv_back);
        tvResName = view.findViewById(R.id.tv_ten_nha_hang);
        tvOrderID = view.findViewById(R.id.tv_ma_don_hang);
        tvStatus = view.findViewById(R.id.tv_trang_thai);
        tvAddress = view.findViewById(R.id.tv_address);
        tvDate = view.findViewById(R.id.tv_date);
        tvPrice = view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = view.findViewById(R.id.tv_tong_tien);
        btnReorder = view.findViewById(R.id.btn_dat_lai);

        viewModel = new ViewModelProvider(getActivity()).get(HistoryDetailViewModel.class);
        recyclerView = view.findViewById(R.id.rec_order_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter();
        recyclerView.setAdapter(adapter);

        updateUI();
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        imvBack.setOnClickListener(this);
        btnReorder.setOnClickListener(this);
    }

    private void updateUI() {
        viewModel.getSelectedOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                adapter.setOrderList(order.getFoodList());
                adapter.notifyDataSetChanged();

                tvResName.setText(order.getRestaurant().getAddress());
                tvOrderID.setText(order.getId());
                tvStatus.setText(order.isComplete() ? "Hoàn thành" : "Bị hủy");
                tvDate.setText(order.getDate());

                double price = 0;
                for (Order_Food food : order.getFoodList())
                {
                    price += food.getPrice() * food.getQuantity();
                }
                tvPrice.setText(String.format("%sđ", String.valueOf(price)));
                tvTotalPrice.setText(String.format("%sđ", String.valueOf(price + 15000)));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imv_back)
            navController.navigate(R.id.orderFragment);
        else {
            NavDirections action = historyDetailFragmentDirections.actionHistoryDetailFragmentToCartFragment(orderID);
            navController.navigate(action);
        }
    }
}