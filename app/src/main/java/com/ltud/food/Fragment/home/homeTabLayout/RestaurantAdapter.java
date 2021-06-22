package com.ltud.food.Fragment.home.homeTabLayout;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ltud.food.Fragment.restaurantDetail.RestaurantDetailFragment;
import com.ltud.food.R;
import com.ltud.food.Model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.homeViewHolder>{


    Context context;
    ArrayList<Restaurant> restaurantArrayList;

    NavController navController;


    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList) {
        this.context = context;
        this.restaurantArrayList = restaurantArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public RestaurantAdapter.homeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_gantoi,parent,false);
        final homeViewHolder vHolder = new homeViewHolder(v);


//        vHolder.item_gantoi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"Test"+ String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
//
//
//                //Navigate fragment
//                navController = Navigation.findNavController(v);
//                navController.navigate(R.id.action_homeFragment_to_restaurantDetailFragment);
//                //Truyen du lieu
//                TextView txt_restaurant_name = (TextView) v.findViewById(R.id.txt_restaurant_name);
//                TextView txt_address_detail = (TextView) v.findViewById(R.id.txt_address_detail);
//                TextView txt_rate_detail = (TextView) v.findViewById(R.id.txt_rate_detail);
//                ImageView img_res_detail = (ImageView) v.findViewById(R.id.img_restaurant_detail);
//
////                txt_restaurant_name.setText(restaurantArrayList.get(vHolder.getAdapterPosition()).getName());
////                txt_address_detail.setText(restaurantArrayList.get(vHolder.getAdapterPosition()).getAddress());
////                txt_rate_detail.setText((int) restaurantArrayList.get(vHolder.getAdapterPosition()).getRate());
////
////                img_res_detail.setImageURI(restaurantArrayList.get(vHolder.getAdapterPosition()).getImg());
//            }
//        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantAdapter.homeViewHolder holder, int position) {

        Restaurant restaurant = restaurantArrayList.get(position);

        holder.name.setText(restaurant.name);
        holder.address.setText(restaurant.address);
        holder.rate.setText(String.valueOf(restaurant.rate));
        Glide.with(holder.restaurant.getContext()).load(restaurant.img).into(holder.restaurant);

        holder.item_gantoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Test"+ String.valueOf(holder.getAdapterPosition()),Toast.LENGTH_SHORT).show();

                //Navigate Detail Fragment
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new RestaurantDetailFragment(restaurant.getName(),restaurant.getAddress(),String.valueOf(restaurant.getRate()),restaurant.getImg())).addToBackStack(null).commit();
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
