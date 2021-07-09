package com.ltud.food.Fragment.Order.HistoryTab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.ltud.food.Adapter.HistoryTabAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.Order.orderFragmentDirections;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.HistoryTab.HistoryTabViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class historyTabFragment extends Fragment implements HistoryTabAdapter.SelectedItem, View.OnClickListener, HistoryDatePickerFragment.onSetedDate, AdapterView.OnItemClickListener {

    private AutoCompleteTextView completeTextViewStatus, completeTextViewDate;
    private MaterialButton btnGetAll;
    private RecyclerView recyclerView;
    private HistoryTabAdapter adapter;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private HistoryTabViewModel viewModel;
    private List<Order> orderList;
    private ArrayAdapter<String> statusAdapter;
    private boolean isComplete;
    private boolean isSelected;
    private String myDate;

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
        progressDialog.show();
        navController = Navigation.findNavController(view);

        completeTextViewStatus = view.findViewById(R.id.actv_complete_status);
        completeTextViewDate = view.findViewById(R.id.actv_date);
        btnGetAll = view.findViewById(R.id.btn_all);

        recyclerView = view.findViewById(R.id.rec_history_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new HistoryTabAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(HistoryTabViewModel.class);
        progressDialog.dismiss();

        completeTextViewDate.setOnClickListener(this);
        btnGetAll.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI() {

        viewModel.getHistoryOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                adapter.setOrderList(orders);
                adapter.notifyDataSetChanged();
                orderList = orders;
            }
        });

        //set up autocomplete
        completeTextViewStatus.setText("Trạng thái");
        statusAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.status));
        completeTextViewStatus.setAdapter(statusAdapter);
        completeTextViewStatus.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_all)
        {
            updateUI();
            myDate = null;
            isSelected = false;
            completeTextViewStatus.setAdapter(statusAdapter);
            completeTextViewStatus.setOnItemClickListener(this);
        }
        else dateFilter();
    }

    private void dateFilter() {
        DialogFragment newFragment = new HistoryDatePickerFragment(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "Ngày");
    }

    @Override
    public void onSelectedItem(int index) {
        NavDirections action = orderFragmentDirections.actionOrderFragmentToHistoryDetailFragment(orderList.get(index).getId());
        navController.navigate(action);
    }

    @Override
    public void onSet(String date) {
        myDate = date;

        if(!isSelected)
        {
            viewModel.getDateOrderFilter(date).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
                @Override
                public void onChanged(List<Order> orders) {
                    adapter.setOrderList(orders);
                    adapter.notifyDataSetChanged();
                    orderList = orders;
                    completeTextViewDate.setText(date);
                    return;
                }
            });
        }
        else {
            viewModel.getOrderFilter(isComplete, date).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
                @Override
                public void onChanged(List<Order> orders) {
                    adapter.setOrderList(orders);
                    adapter.notifyDataSetChanged();
                    orderList = orders;
                    return;
                }
            });
        }

        adapter.setOrderList(new ArrayList<>());
        adapter.notifyDataSetChanged();
        //viewModel.getDateOrderFilter(date);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        isComplete = position == 0;
        isSelected = true;
        if(myDate == null)
        {
            viewModel.getStatusOrderFilter(isComplete).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
                @Override
                public void onChanged(List<Order> orders) {
                    adapter.setOrderList(orders);
                    adapter.notifyDataSetChanged();
                    orderList = orders;
                    return;
                }
            });
        }
        else {
            viewModel.getOrderFilter(isComplete, myDate).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
                @Override
                public void onChanged(List<Order> orders) {
                    adapter.setOrderList(orders);
                    adapter.notifyDataSetChanged();
                    orderList = orders;
                    return;
                }
            });
        }

        adapter.setOrderList(new ArrayList<>());
        adapter.notifyDataSetChanged();
    }
}