package com.ltud.food.Fragment.Order.DeliveringTab;

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

import com.ltud.food.Adapter.DeliveringTabAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.Order.orderFragmentDirections;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.DeliveringTab.DeliveringTabViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class deliveringTabFragment extends Fragment implements DeliveringTabAdapter.SelectedItem{

    private RecyclerView recyclerView;
    private DeliveringTabAdapter adapter;
    private DeliveringTabViewModel viewModel;
    private CustomProgressDialog progressDialog;
    private List<Order> orderList;
    private NavController navController;

    public deliveringTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deliverying_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();
        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.rec_delivering_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeliveringTabAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(DeliveringTabViewModel.class);
        viewModel.getDeliveringOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                orderList = list;
                adapter.setOrderList(list);
                adapter.notifyDataSetChanged();
            }
        });

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onSelectedItem(int index) {
        NavDirections action = orderFragmentDirections.actionOrderFragmentToOrderDetailFragment(orderList.get(index).getId());
        navController.navigate(action);
    }
}