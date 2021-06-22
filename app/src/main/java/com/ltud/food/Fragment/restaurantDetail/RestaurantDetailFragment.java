package com.ltud.food.Fragment.restaurantDetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ltud.food.Fragment.home.homeFragment;
import com.ltud.food.R;

public class RestaurantDetailFragment extends Fragment {

    String name, address,img;
    String rate;

    public RestaurantDetailFragment() {
        // Required empty public constructor
    }

    public RestaurantDetailFragment(String name, String address, String rate, String img) {
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.img = img;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        TextView txt_restaurant_name = v.findViewById(R.id.txt_restaurant_name);
        TextView txt_address = v.findViewById(R.id.txt_address_detail);
        TextView txt_rate = v.findViewById(R.id.txt_rate_detail);
        ImageView img_detail = v.findViewById(R.id.img_restaurant_detail);

        txt_restaurant_name.setText(name);
        txt_address.setText(address);
        txt_rate.setText(rate);
        Glide.with(getContext()).load(img).into(img_detail);

        return v;
    }

//    public void onBackPressed(){
//        AppCompatActivity activity = (AppCompatActivity)getContext();
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, new homeFragment()).addToBackStack(null).commit();
//    }
}