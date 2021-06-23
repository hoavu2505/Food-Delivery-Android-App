package com.ltud.food.Fragment.restaurantDetail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ltud.food.Fragment.home.homeFragment;
import com.ltud.food.Fragment.order.orderFragment;
import com.ltud.food.Fragment.restaurantDetail.detailTabLayout.detail_danhgiaFragment;
import com.ltud.food.Fragment.restaurantDetail.detailTabLayout.detail_datdonFragment;
import com.ltud.food.Fragment.restaurantDetail.detailTabLayout.detail_thongtinFragment;
import com.ltud.food.R;
import com.ltud.food.orderTabLayout.hientaiFragment;
import com.ltud.food.orderTabLayout.lichsuFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantDetailFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    resdetailAdapter adapter;
    ImageView img_back;


    public String id, name, address,img;
    public String rate;


    public RestaurantDetailFragment() {
        // Required empty public constructor
    }

    public RestaurantDetailFragment(String id,String name, String address, String rate, String img) {
        this.id = id;
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



    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);


        img_back = view.findViewById(R.id.img_res_detail_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, new homeFragment()).addToBackStack(null).commit();

//                navController.navigate(R.id.action_restaurantDetailFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = getView().findViewById(R.id.tablayout_res_detail);
        viewPager = getView().findViewById(R.id.viewpager_res_detail);


        //Add fragment vao tablayout
        adapter = new RestaurantDetailFragment.resdetailAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new detail_datdonFragment(),"Đặt đơn");
        adapter.AddFragment(new detail_danhgiaFragment(),"Đánh giá");
        adapter.AddFragment(new detail_thongtinFragment(),"Thông tin");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private class resdetailAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String>   stringArrayList   = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public resdetailAdapter(@NotNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }

        public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, new homeFragment()).addToBackStack(null).commit();

    }

}