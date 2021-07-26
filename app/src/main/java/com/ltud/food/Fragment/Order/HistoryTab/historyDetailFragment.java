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

import com.google.android.material.button.MaterialButton;
import com.ltud.food.Adapter.CheckoutAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.Order.orderFragment;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.HistoryTab.HistoryDetailViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class historyDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imvBack;
    private TextView tvResName, tvOrderID, tvStatus, tvLocation, tvDate, tvPrice, tvTotalPrice;
    private RecyclerView recyclerView;
    private MaterialButton btnReorder, btnRemove;
    private CheckoutAdapter adapter;
    private CustomProgressDialog progressDialog;
    private HistoryDetailViewModel viewModel;
    private NavController navController;
    private String orderID;
    private List<Order> orderList;
    private Order currentOrder;

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

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();
        navController = Navigation.findNavController(view);
        orderID = historyDetailFragmentArgs.fromBundle(getArguments()).getOrderID();
        orderList = orderFragment.orderList;

        imvBack = view.findViewById(R.id.imv_back);
        tvResName = view.findViewById(R.id.tv_ten_nha_hang);
        tvOrderID = view.findViewById(R.id.tv_ma_don_hang);
        tvStatus = view.findViewById(R.id.tv_trang_thai);
        tvLocation = view.findViewById(R.id.tv_location);
        tvDate = view.findViewById(R.id.tv_date);
        tvPrice = view.findViewById(R.id.tv_tien_hang);
        tvTotalPrice = view.findViewById(R.id.tv_tong_tien);
        btnReorder = view.findViewById(R.id.btn_dat_lai);
        btnRemove = view.findViewById(R.id.btn_xoa);

        viewModel = new ViewModelProvider(getActivity()).get(HistoryDetailViewModel.class);
        recyclerView = view.findViewById(R.id.rec_order_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter();
        recyclerView.setAdapter(adapter);

        updateUI();

        imvBack.setOnClickListener(this);
        btnReorder.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
    }

    private void updateUI() {
        viewModel.getSelectedOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                adapter.setOrderList(order.getFoodList());
                adapter.notifyDataSetChanged();

                tvResName.setText(order.getRestaurant().getAddress());
                tvOrderID.setText(order.getId());
                tvLocation.setText(order.getLocation());
                tvStatus.setText(order.isComplete() ? "Hoàn thành" : "Bị hủy");
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

                currentOrder = order;
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.imv_back)
        {
            navController.navigate(R.id.orderFragment);
        }
        else if(v.getId() == R.id.btn_dat_lai)
            reorderAction();
        else {
            viewModel.removeAnOrder(currentOrder.getId());
            navController.navigate(R.id.orderFragment);
        }
    }

    private void reorderAction() {
        if(!orderList.isEmpty())
        {
            for(Order passingOrder : orderList)
            {
                if(passingOrder.getStatus() == 0 && passingOrder.getRestaurant().getId().equals(currentOrder.getRestaurant().getId()))
                    viewModel.removeAnOrder(passingOrder.getId());
            }
        }
        viewModel.createNewOrder(currentOrder.getRestaurant(), currentOrder.getFoodList());

        NavDirections action = historyDetailFragmentDirections.actionHistoryDetailFragmentToRestaurantDetailFragment(
                currentOrder.getRestaurant().name, currentOrder.getRestaurant().address, currentOrder.getRestaurant().id,
                currentOrder.getRestaurant().img, (float) currentOrder.getRestaurant().rate
        ).setOrderID(orderID);
        navController.navigate(action);
    }
}