package com.ltud.food.Fragment.Notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ltud.food.Adapter.NotifyAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Order;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Notification.NotifyViewModel;

import java.util.List;

public class notiFragment extends Fragment implements NotifyAdapter.SelectedItem, View.OnClickListener {

    private ViewGroup layout;
    private TextView tvReadAll;
    private RecyclerView recyclerView;
    private NotifyAdapter adapter;
    private NotifyViewModel viewModel;
    private List<Order> orderList;
    private NavController navController;

    public notiFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Xu ly null id user
        navController = Navigation.findNavController(view);
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
            return;
        }

        CustomProgressDialog progressDialog = new CustomProgressDialog(getContext());
        progressDialog.show();

        layout = view.findViewById(R.id.layout);
        tvReadAll = view.findViewById(R.id.tv_read_all);
        recyclerView = view.findViewById(R.id.rec_notify_list);

        adapter = new NotifyAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(NotifyViewModel.class);
        viewModel.getOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                adapter.setOrderList(orders);
                adapter.notifyDataSetChanged();
                orderList = orders;
                if(!orders.isEmpty())
                    layout.setVisibility(View.GONE);
                else
                    layout.setVisibility(View.VISIBLE);
            }
        });

        if(progressDialog.isShowing())
            progressDialog.dismiss();

        tvReadAll.setOnClickListener(this);
    }

    @Override
    public void onClickedItem(int index) {
        viewModel.updateCheckedNotify(orderList.get(index).getId(), index);
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        while (!orderList.isEmpty())
        {
            viewModel.updateCheckedNotify(orderList.get(index).getId(), index);
        }
    }
}