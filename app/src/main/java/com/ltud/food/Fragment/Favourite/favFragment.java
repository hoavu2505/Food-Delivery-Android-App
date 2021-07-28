package com.ltud.food.Fragment.Favourite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Adapter.FavouriteAdapter;
import com.ltud.food.Model.Favourite;
import com.ltud.food.R;
import com.ltud.food.ViewModel.RestaurantDetail.FavouriteViewModel;

import java.util.ArrayList;

public class favFragment extends Fragment {

    private ViewGroup layout;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private FavouriteViewModel viewModel;
    private NavController navController;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseFirestore db;
    ArrayList<Favourite> favouriteList;

    public favFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false);
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

        layout = view.findViewById(R.id.layout);


        recyclerView = view.findViewById(R.id.rec_favourite_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        layout = view.findViewById(R.id.layout);

        favouriteList = new ArrayList<Favourite>();
        adapter = new FavouriteAdapter(getActivity(), favouriteList);


        recyclerView.setAdapter(adapter);

//        viewModel = new ViewModelProvider(getActivity()).get(FavouriteViewModel.class);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Customer").document(userID)
                .collection("Favourite")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                favouriteList.add(dc.getDocument().toObject(Favourite.class));
                            }

                            adapter.notifyDataSetChanged();
                            if (!favouriteList.isEmpty())
                            {
                                layout.setVisibility(getView().GONE);
                            }
                            else{
                                layout.setVisibility(getView().VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(adapter);
    }
}