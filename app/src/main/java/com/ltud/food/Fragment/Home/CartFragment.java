package com.ltud.food.Fragment.Home;

import android.os.Bundle;

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
import com.ltud.food.Adapter.CartListAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Home.CartViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartFragment extends Fragment implements CartListAdapter.ChangeQuantity, View.OnClickListener {

    private CartListAdapter adapter;
    private CartViewModel cartViewModel;
    private TextView tvTotalPrice;
    private ImageView imvClose;
    private List<Order> orderList;
    private NavController navController;
    private CustomProgressDialog progressDialog;

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

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        cartViewModel.getOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                orderList = orders;
                adapter.setOrderList(orders);
                adapter.notifyDataSetChanged();

                double totalPrice = 0;
                for (int i = 0; i < orders.size(); i++) {
                    totalPrice += orders.get(i).getFood().getPrice() * orders.get(i).getQuantity();
                }
                tvTotalPrice.setText(String.format("%sđ", totalPrice));
                progressDialog.dismiss();
            }
        });

        btnThanhtoan.setOnClickListener(this);
        imvClose.setOnClickListener(this);
    }

    @Override
    public void onIncreaseQuantity(int index) {
        int quantity = orderList.get(index).getQuantity();
        orderList.get(index).setQuantity(quantity + 1);
        cartViewModel.updateOrderQuantity(orderList.get(index).getId(), index, quantity+1);
    }

    @Override
    public void onDecreaseQuantity(int index) {
        int quantity = orderList.get(index).getQuantity();
        if(quantity == 1)
            cartViewModel.deleteOneOrder(orderList.get(index).getId(), index);
        else
            cartViewModel.updateOrderQuantity(orderList.get(index).getId(), index, quantity-1);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_thanh_toan)
        {
            navController.navigate(R.id.checkoutFragment);
        }
        else{

        }
    }
}