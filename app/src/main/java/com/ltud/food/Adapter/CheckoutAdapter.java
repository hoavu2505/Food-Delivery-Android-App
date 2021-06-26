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
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ItemViewHolder> {

    private List<Order> orderList;

    public void setOrderList(List<Order> orders)
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
                .load(orderList.get(position).getFood().getImg())
                .into(holder.imageFood);
        holder.tvName.setText(orderList.get(position).getFood().getName().toUpperCase());
        holder.tvPrice.setText(String.format("%sđ", String.valueOf(orderList.get(position).getFood().getPrice())));
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

            imageFood = (ImageView) itemView.findViewById(R.id.imv_order_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_ten_mon);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_gia);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_so_luong);
        }
    }
}
