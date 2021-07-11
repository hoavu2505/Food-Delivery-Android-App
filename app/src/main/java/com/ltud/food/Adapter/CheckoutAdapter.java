package com.ltud.food.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltud.food.Model.Order;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ItemViewHolder> {

    private List<Order_Food> orderList;

    public void setOrderList(List<Order_Food> orders)
    {
        orderList = orders;
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ItemViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(orderList.get(position).getImg())
                .into(holder.imageFood);
        holder.tvName.setText(orderList.get(position).getName());
        holder.tvPrice.setText(String.format("%sđ", orderList.get(position).getPrice()));
        holder.tvQuantity.setText(String.valueOf(orderList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        else
            return orderList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView tvName, tvPrice, tvQuantity;

        public ItemViewHolder(@NotNull View itemView) {
            super(itemView);

            imageFood = itemView.findViewById(R.id.imv_order_image);
            tvName = itemView.findViewById(R.id.tv_ten_mon);
            tvPrice = itemView.findViewById(R.id.tv_gia);
            tvQuantity = itemView.findViewById(R.id.tv_so_luong);
        }
    }
}
