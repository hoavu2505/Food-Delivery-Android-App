package com.ltud.food.Fragment.restaurantDetail.detailTabLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltud.food.Model.Food;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.foodViewHolder> {

    Context context;
    ArrayList<Food> foodArrayList;

    public FoodAdapter(Context context, ArrayList<Food> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public foodViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        final foodViewHolder vHolder = new foodViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull foodViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        holder.name.setText(food.name);
        holder.price.setText(String.valueOf(food.price));
        holder.rate.setText(String.valueOf(food.rate));
        Glide.with(holder.food.getContext()).load(food.img).into(holder.food);
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class foodViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, rate;
        ImageView food;

        CardView item_food;

        public foodViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            item_food = (CardView) itemView.findViewById(R.id.item_food_id);
            name = itemView.findViewById(R.id.txt_foodname);
            rate = itemView.findViewById(R.id.txt_foodrate);
            price = itemView.findViewById(R.id.txt_foodprice);
            food = itemView.findViewById(R.id.img_food);

        }

    }
}
