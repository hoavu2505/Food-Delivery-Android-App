package com.ltud.food.Fragment.Order.HistoryTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import com.ltud.food.Adapter.HistoryTabAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.Order.orderFragmentDirections;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.HistoryTab.HistoryTabViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class historyTabFragment extends Fragment implements HistoryTabAdapter.SelectedItem {

    private RecyclerView recyclerView;
    private HistoryTabAdapter adapter;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private HistoryTabViewModel viewModel;
    private List<Order> orderList;

    public historyTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomProgressDialog(getContext());
        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.rec_history_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new HistoryTabAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(HistoryTabViewModel.class);
    }

    @Override
    public void onSelectedItem(int index) {
        NavDirections action = orderFragmentDirections.actionOrderFragmentToHistoryDetailFragment(orderList.get(index).getId());
        navController.navigate(action);
    }

    @Override
    public void onResume() {
        super.onResume();


        updateUI();
    }

    private void updateUI() {
        progressDialog.show();
        viewModel.getHistoryOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                adapter.setOrderList(orders);
                adapter.notifyDataSetChanged();
                orderList = orders;
            }
        });
        progressDialog.dismiss();
    }

}