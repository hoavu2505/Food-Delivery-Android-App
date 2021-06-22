package com.ltud.food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ltud.food.Model.Gender;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenderSpinnerAdapter extends ArrayAdapter<Gender> {

    public GenderSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Gender> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gender_layout, parent, false);
        TextView tvGender = (TextView) convertView.findViewById(R.id.tv_gender);
        Gender gender = this.getItem(position);
        if(gender != null)
            tvGender.setText(gender.getGender());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView,@NotNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gender_item_layout, parent, false);
        TextView tvGenderSelected = (TextView) convertView.findViewById(R.id.tv_gender_item);
        Gender gender = this.getItem(position);
        if(gender != null)
            tvGenderSelected.setText(gender.getGender());

        return convertView;
    }


}
