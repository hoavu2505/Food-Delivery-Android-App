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

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolderItem> {

    private List<Order> orderList;

    public void setOrderList(List<Order> orders)
    {
        this.orderList = orders;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item_layout, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderItem holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(orderList.get(position).getRestaurant().getImg())
                .into(holder.imageView);
        if(orderList.get(position).getStatus() == 1)
        {
            holder.tvTitle.setText("Đặt hàng thành công");
            holder.tvNotify.setText(String.format("Đơn hàng tại %s - %s được đặt thành công và sẽ được giao bởi Eat Now",
                    orderList.get(position).getRestaurant().getName(), orderList.get(position).getRestaurant().getAddress()));
        }

        if(orderList.get(position).getStatus() == 2)
        {
            if (orderList.get(position).isComplete())
            {
                holder.tvTitle.setText(String.format("Đơn hàng tại %s - %s đã hoàn tất",
                        orderList.get(position).getRestaurant().getName(), orderList.get(position).getRestaurant().getAddress()));
                holder.tvNotify.setText(String.format("Đơn hàng đã được giao đến địa chỉ %s. Cảm ơn bạn đã sử dụng dịch vụ Eat Now. " +
                                "Hãy chia sẻ cảm nhận của bạn và tiếp tục đặt hàng nhé !", orderList.get(position).getLocation()));
            }
            else {
                holder.tvTitle.setText(String.format("Đơn hàng tại %s - %s đã được hủy",
                        orderList.get(position).getRestaurant().getName(), orderList.get(position).getRestaurant().getAddress()));
                holder.tvNotify.setText("Cảm ơn bạn đã sử dụng dịch vụ Eat Now. Hãy ấn đặt lại đơn hàng bất cứ khi nào bạn muốn !");
            }
        }
        holder.tvDate.setText(orderList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        else return orderList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvNotify, tvDate;
        public ViewHolderItem(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imv_notify);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvNotify = itemView.findViewById(R.id.tv_notify);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
