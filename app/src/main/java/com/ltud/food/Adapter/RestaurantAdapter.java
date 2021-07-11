package com.ltud.food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltud.food.Fragment.Home.homeFragmentDirections;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.homeViewHolder>{


    Context context;
    ArrayList<Restaurant> restaurantArrayList;


    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList) {
        this.context = context;
        this.restaurantArrayList = restaurantArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public homeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_gantoi,parent,false);
        final homeViewHolder vHolder = new homeViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull homeViewHolder holder, int position) {

        Restaurant restaurant = restaurantArrayList.get(position);

        holder.name.setText(restaurant.name);
        holder.address.setText(restaurant.address);
        holder.rate.setText(String.valueOf(restaurant.rate));
        Glide.with(holder.restaurant.getContext()).load(restaurant.img).into(holder.restaurant);

        holder.item_gantoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = homeFragmentDirections.actionHomeFragmentToRestaurantDetailFragment(restaurant.name, restaurant.address, restaurant.id, restaurant.img, (float) restaurant.rate);

                NavController navController = Navigation.findNavController(v);
                navController.navigate(action);
           }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }

    public class homeViewHolder extends RecyclerView.ViewHolder{

        TextView name, address, rate;
        ImageView restaurant;

        CardView item_gantoi;

        public homeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            item_gantoi = (CardView) itemView.findViewById(R.id.item_gantoi_id);
            name = itemView.findViewById(R.id.txt_name);
            rate = itemView.findViewById(R.id.txt_rate);
            address = itemView.findViewById(R.id.txt_address);
            restaurant = itemView.findViewById(R.id.img_restaurant);

        }

    }

}
