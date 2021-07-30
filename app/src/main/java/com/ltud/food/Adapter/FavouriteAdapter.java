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
import com.ltud.food.Fragment.Favourite.favFragmentDirections;
import com.ltud.food.Model.Favourite;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolderItem> {

    Context context;
    ArrayList<Favourite> favouriteList;

    public FavouriteAdapter(Context context, ArrayList<Favourite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gantoi, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderItem holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(favouriteList.get(position).getRestaurant().getImg())
                .into(holder.restaurant);
        holder.name.setText(favouriteList.get(position).getRestaurant().getName());
        holder.address.setText(favouriteList.get(position).getRestaurant().getAddress());
        holder.rate.setText(String.format("%.1f",favouriteList.get(position).getRestaurant().getRate()));

        holder.item_gantoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = favFragmentDirections.actionFavFragmentToRestaurantDetailFragment(
                        favouriteList.get(position).getRestaurant().getName(),
                        favouriteList.get(position).getRestaurant().getAddress(),
                        favouriteList.get(position).getRestaurant().getId(),
                        favouriteList.get(position).getRestaurant().getImg(),
                        (float) favouriteList.get(position).getRestaurant().getRate()
                );
                NavController navController = Navigation.findNavController(v);
                navController.navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(favouriteList == null)
            return 0;
        else return favouriteList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView name, address, rate;
        ImageView restaurant;

        CardView item_gantoi;

        public ViewHolderItem(@NonNull @NotNull View itemView) {
            super(itemView);

            item_gantoi = (CardView) itemView.findViewById(R.id.item_gantoi_id);
            name = itemView.findViewById(R.id.txt_name);
            rate = itemView.findViewById(R.id.txt_rate);
            address = itemView.findViewById(R.id.txt_address);
            restaurant = itemView.findViewById(R.id.img_restaurant);
        }
    }
}
