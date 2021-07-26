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

import com.airbnb.lottie.L;
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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartListAdapter.ChangeQuantity, View.OnClickListener {

    private CartListAdapter adapter;
    private CartViewModel cartViewModel;
    private TextView tvTotalPrice;
    private ImageView imvClose;
    private Order currentOrder = new Order();
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private String orderID;

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
        progressDialog.show();
        orderID = CartFragmentArgs.fromBundle(getArguments()).getOrderID();

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        cartViewModel.getCurrentOrder(orderID).observe(getViewLifecycleOwner(), new Observer<Order>() {
                @Override
                public void onChanged(Order order) {
                    adapter.setOrderList(order.getFoodList());
                    adapter.notifyDataSetChanged();

                    int totalPrice = 0;
                    for (Order_Food food : order.getFoodList())
                    {
                        totalPrice += food.getPrice() * food.getQuantity();
                    }
                    Locale vietnam = new Locale("vi", "VN");
                    NumberFormat dongFormat = NumberFormat.getCurrencyInstance(vietnam);
                    tvTotalPrice.setText(dongFormat.format(totalPrice));

                    currentOrder = order;
                    progressDialog.dismiss();
                }
            });

        btnThanhtoan.setOnClickListener(this);
        imvClose.setOnClickListener(this);
    }

    @Override
    public void onIncreaseQuantity(int index) {
        long quantity = currentOrder.getFoodList().get(index).getQuantity() + 1;
        currentOrder.getFoodList().get(index).setQuantity(quantity);
        cartViewModel.updateFoodQuantity(orderID, currentOrder.getFoodList());
    }

    @Override
    public void onDecreaseQuantity(int index) {
        long quantity = currentOrder.getFoodList().get(index).getQuantity() - 1;
        if(quantity == 0)
        {
            if (currentOrder.getFoodList().size() == 1)
            {
                NavDirections action = CartFragmentDirections.actionCartFragmentToRestaurantDetailFragment(
                        currentOrder.getRestaurant().name, currentOrder.getRestaurant().address, currentOrder.getRestaurant().id,
                        currentOrder.getRestaurant().img, (float) currentOrder.getRestaurant().rate
                );
                navController.navigate(action);
                cartViewModel.deleteOneOrder(orderID);
            }
            else
                cartViewModel.deleteOneFood(orderID, index, currentOrder.getFoodList().get(index));
        }
        else
        {
            currentOrder.getFoodList().get(index).setQuantity(quantity);
            cartViewModel.updateFoodQuantity(orderID, currentOrder.getFoodList());
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
                    currentOrder.getRestaurant().name, currentOrder.getRestaurant().address, currentOrder.getRestaurant().id,
                    currentOrder.getRestaurant().img, (float) currentOrder.getRestaurant().rate
            );
            navController.navigate(action);
        }
    }
}