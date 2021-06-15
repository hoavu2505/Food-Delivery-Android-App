package homeTabLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltud.food.R;
import com.ltud.food.Restaurant;

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
    public RestaurantAdapter.homeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_gantoi,parent,false);

        return new homeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantAdapter.homeViewHolder holder, int position) {

        Restaurant restaurant = restaurantArrayList.get(position);

        holder.name.setText(restaurant.name);
        holder.address.setText(restaurant.address);
        holder.rate.setText(String.valueOf(restaurant.rate));
        Glide.with(holder.restaurant.getContext()).load(restaurant.img).into(holder.restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }

    public static class homeViewHolder extends RecyclerView.ViewHolder{

        TextView name, address, rate;
        ImageView restaurant;

        public homeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            rate = itemView.findViewById(R.id.txt_rate);
            address = itemView.findViewById(R.id.txt_address);
            restaurant = itemView.findViewById(R.id.img_restaurant);
        }
    }
}
