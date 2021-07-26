package com.ltud.food.Fragment.Order.HistoryTab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class historyTabFragment extends Fragment implements HistoryTabAdapter.SelectedItem, View.OnClickListener, HistoryDatePickerFragment.onSetedDate, AdapterView.OnItemClickListener {

    private ViewGroup layout;
    private AutoCompleteTextView completeTextViewStatus, completeTextViewDate;
    private TextView tvGetAll, tvRemoveAll;
    private RecyclerView recyclerView;
    private HistoryTabAdapter adapter;
    private NavController navController;
    private CustomProgressDialog progressDialog;
    private HistoryTabViewModel viewModel;
    private List<Order> orderList;
    private ArrayAdapter<String> statusAdapter;
    private boolean isComplete;
    private boolean isSelected;
    private Date myDate;

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

        layout = view.findViewById(R.id.layout);
        tvGetAll = view.findViewById(R.id.tv_get_all);
        tvRemoveAll = view.findViewById(R.id.tv_remove_all);
        completeTextViewStatus = view.findViewById(R.id.actv_complete_status);
        completeTextViewDate = view.findViewById(R.id.actv_date);

        recyclerView = view.findViewById(R.id.rec_history_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new HistoryTabAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(HistoryTabViewModel.class);
        progressDialog.dismiss();

        completeTextViewDate.setOnClickListener(this);
        tvGetAll.setOnClickListener(this);
        tvRemoveAll.setOnClickListener(this);
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
                if(!orders.isEmpty())
                    layout.setVisibility(View.GONE);
                else
                    layout.setVisibility(View.VISIBLE);
                orderList = orders;
                adapter.setOrderList(orderList);
                adapter.notifyDataSetChanged();
            }
        });

        //set up autocomplete
        myDate = null;
        isSelected = false;
        completeTextViewDate.setText("Ngày");
        completeTextViewStatus.setText("Trạng thái");
        statusAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.status));
        completeTextViewStatus.setAdapter(statusAdapter);
        completeTextViewStatus.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_get_all)
        {
            updateUI();
        }
        else if(v.getId() == R.id.tv_remove_all)
        {
            removeHistory();
        }
        else dateFilter();
    }

    private void removeHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setIcon(R.drawable.history_image)
                .setMessage("Sau khi xóa toàn bộ lịch sử sẽ không thể khôi phục !")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        int index = 0;
                        while (!orderList.isEmpty())
                            viewModel.removeAnOrder(orderList.get(index).getId(), index);
                        progressDialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
    public void onSet(Date date) {
        myDate = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        completeTextViewDate.setText(dateFormat.format(date));

        if(!isSelected)
        {
            viewModel.getDateOrderFilter(date).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
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