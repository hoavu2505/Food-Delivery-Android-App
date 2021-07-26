package com.ltud.food.Fragment.Order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltud.food.Adapter.DeliveringTabAdapter;
import com.ltud.food.Adapter.DraftTabAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Order.DeliveringTab.DeliveringTabViewModel;
import com.ltud.food.ViewModel.Order.DraftTabViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class draftTabFragment extends Fragment implements DraftTabAdapter.SelectedItem, View.OnClickListener {

    private ViewGroup layout;
    private TextView tvRemoveAll;
    private RecyclerView recyclerView;
    private DraftTabAdapter adapter;
    private DraftTabViewModel viewModel;
    private CustomProgressDialog progressDialog;
    private List<Order> orderList;
    private NavController navController;

    public draftTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();
        navController = Navigation.findNavController(view);

        layout = view.findViewById(R.id.layout);
        tvRemoveAll = view.findViewById(R.id.tv_remove_all);
        recyclerView = view.findViewById(R.id.rec_draft_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DraftTabAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(DraftTabViewModel.class);
        viewModel.getDraftOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                if(!list.isEmpty())
                    layout.setVisibility(View.GONE);
                else
                    layout.setVisibility(View.VISIBLE);

                orderList = list;
                adapter.setOrderList(orderList);
                adapter.notifyDataSetChanged();
            }
        });

        tvRemoveAll.setOnClickListener(this);

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onButtonClicked(int index) {
        viewModel.removeDraftOrder(orderList.get(index).getId(), index);
    }

    @Override
    public void onClickedItem(int index) {
        NavDirections action = orderFragmentDirections.actionOrderFragmentToRestaurantDetailFragment(
                orderList.get(index).getRestaurant().name, orderList.get(index).getRestaurant().address, orderList.get(index).getRestaurant().id,
                orderList.get(index).getRestaurant().img, (float) orderList.get(index).getRestaurant().rate
        );
        navController.navigate(action);
    }

    @Override
    public void onClick(View v) {
        removeDraft();
    }

    private void removeDraft() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setIcon(R.drawable.history_image)
                .setMessage("Sau khi xóa toàn bộ đơn hàng sẽ không thể khôi phục !")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        int index = 0;
                        while (!orderList.isEmpty())
                            viewModel.removeDraftOrder(orderList.get(index).getId(), index);
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
}