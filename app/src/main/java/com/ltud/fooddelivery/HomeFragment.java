package com.ltud.fooddelivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment{

    ImageView com,douong,anvat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        com = (ImageView) view.findViewById(R.id.img_com);
        douong = (ImageView) view.findViewById(R.id.img_douong);
        anvat = (ImageView) view.findViewById(R.id.img_anvat);

        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.setImageResource(R.drawable.com_picked);
                douong.setImageResource(R.drawable.douong);
                anvat.setImageResource(R.drawable.anvat);
            }
        });

        douong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.setImageResource(R.drawable.com);
                douong.setImageResource(R.drawable.douong_picked);
                anvat.setImageResource(R.drawable.anvat);
            }
        });

        anvat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.setImageResource(R.drawable.com);
                douong.setImageResource(R.drawable.douong);
                anvat.setImageResource(R.drawable.anvat_picked);
            }
        });

        return view;
    }


}
