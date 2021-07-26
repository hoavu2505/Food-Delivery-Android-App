package com.ltud.food.Adapter;

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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.foodViewHolder> {

    public Context context;
    ArrayList<Food> foodArrayList;

    int itemCount;

    private AddCallbacks addCallbacks;

    public void setCallback(AddCallbacks addCallbacks) {
        this.addCallbacks = addCallbacks;
    }

    public FoodAdapter(){
    }


    public FoodAdapter(Context context, ArrayList<Food> foodArrayList, AddCallbacks addCallbacks) {
        this.context = context;
        this.foodArrayList = foodArrayList;
        this.addCallbacks = addCallbacks;
    }

    @NotNull
    @Override
    public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        final foodViewHolder vHolder = new foodViewHolder(v);

        return vHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull foodViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        holder.name.setText(food.getName().toUpperCase());
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat dongFormat = NumberFormat.getCurrencyInstance(vietnam);
        holder.price.setText(dongFormat.format(food.getPrice()));
        holder.rate.setText(String.valueOf(food.getRate()));
        Glide.with(holder.food.getContext()).load(food.getImg()).into(holder.food);
        holder.add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCallbacks.onAddProduct(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public interface AddCallbacks {
        public void onAddProduct(int pos);
    }

    public class foodViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, rate;
        ImageView food, add_food;

        CardView item_food;

        public foodViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            item_food = (CardView) itemView.findViewById(R.id.item_food_id);
            name = itemView.findViewById(R.id.txt_foodname);
            rate = itemView.findViewById(R.id.txt_foodrate);
            price = itemView.findViewById(R.id.txt_foodprice);
            food = itemView.findViewById(R.id.img_food);

            add_food = itemView.findViewById(R.id.img_add);

        }

    }
}
