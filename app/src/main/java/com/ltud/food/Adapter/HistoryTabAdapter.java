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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryTabAdapter extends RecyclerView.Adapter<HistoryTabAdapter.ItemViewHolder> {

    private List<Order> orderList;
    SelectedItem fragment;

    public HistoryTabAdapter(SelectedItem context) {
        this.fragment = context;
    }

    public void setOrderList(List<Order> orderList)
    {
        this.orderList = orderList;
    }

    public interface SelectedItem
    {
        void onSelectedItem(int index);
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(orderList.get(position).getRestaurant().getImg())
                .into(holder.imgRestaurant);
        holder.tvOrderID.setText(orderList.get(position).getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.tvDate.setText(dateFormat.format(orderList.get(position).getDate()));
        holder.tvName.setText(orderList.get(position).getRestaurant().getName());
        holder.tvAddress.setText(orderList.get(position).getRestaurant().getAddress());

        long totalPrice = 15000;
        long quantity = 0;
        for (Order_Food food : orderList.get(position).getFoodList())
        {
            totalPrice += food.getPrice() * food.getQuantity();
            quantity += food.getQuantity();
        }
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat dongFormat = NumberFormat.getCurrencyInstance(vietnam);
        holder.tvPrice.setText(dongFormat.format(totalPrice));
        holder.tvQuantity.setText(String.format("(%d phần)", quantity));
        holder.tvPaymentMethod.setText(orderList.get(position).getPayment_method() == 0 ? "Ví điện tử" : "Tiền mặt");
        holder.tvIsComplete.setText(orderList.get(position).isComplete() ? "Hoàn thành" : "Bị hủy");
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        else return orderList.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRestaurant;
        TextView tvOrderID, tvDate, tvName, tvAddress, tvPrice, tvQuantity, tvPaymentMethod, tvIsComplete;

        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgRestaurant = itemView.findViewById(R.id.imv_mon_an);
            tvOrderID = itemView.findViewById(R.id.tv_ma_don_hang);
            tvDate = itemView.findViewById(R.id.tv_ngay);
            tvName = itemView.findViewById(R.id.tv_ten_nha_hang);
            tvAddress = itemView.findViewById(R.id.tv_dia_chi);
            tvPrice = itemView.findViewById(R.id.tv_don_gia);
            tvQuantity = itemView.findViewById(R.id.tv_so_luong);
            tvPaymentMethod = itemView.findViewById(R.id.tv_phuong_thuc);
            tvIsComplete = itemView.findViewById(R.id.tv_trang_thai);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onSelectedItem(getAdapterPosition());
                }
            });
        }
    }
}
