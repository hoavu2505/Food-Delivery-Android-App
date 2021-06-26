package com.ltud.food.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ItemViewHolder> {

    private List<Order> orderList;
    private ChangeQuantity changeQuantity;

    public CartListAdapter(ChangeQuantity changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public void setOrderList(List<Order> orders)
    {
        this.orderList = orders;
    }

    public interface ChangeQuantity
    {
        void onIncreaseQuantity(int index);
        void onDecreaseQuantity(int index);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imvItemImage, imvDecrease, imvIncrease;
        TextView tvName, tvRate, tvPrice, tvQuantity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imvItemImage = (ImageView) itemView.findViewById(R.id.imv_order_image);
            imvDecrease = (ImageView) itemView.findViewById(R.id.imv_decrease);
            imvIncrease = (ImageView) itemView.findViewById(R.id.imv_increase);
            tvName = (TextView) itemView.findViewById(R.id.tv_ten_mon);
            tvRate= (TextView) itemView.findViewById(R.id.tv_rate);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_gia);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_so_luong);

            imvIncrease.setOnClickListener(this);
            imvDecrease.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.imv_increase)
                changeQuantity.onIncreaseQuantity(getAdapterPosition());
            else
                changeQuantity.onDecreaseQuantity(getAdapterPosition());
        }
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ItemViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(orderList.get(position).getFood().getImg())
                .into(holder.imvItemImage);
        holder.tvName.setText(orderList.get(position).getFood().getName().toUpperCase());
        holder.tvRate.setText(String.format("%d+ lượt thích", orderList.get(position).getFood().getRate()));
        holder.tvPrice.setText(String.format("%sđ", orderList.get(position).getFood().getPrice()));
        holder.tvQuantity.setText(String.valueOf(orderList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        else
            return orderList.size();
    }


}
