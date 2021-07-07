package com.ltud.food.Fragment.RestaurantDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantDetailFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    resdetailAdapter adapter;
    ImageView img_back;

//    private RestaurantDetailListener listener;

    public static String id, name, address, img, orderID;
    public static float rate;

    public String get_resId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getImg() {
        return img;
    }

    public String getOrderID()
    {
        return orderID;
    }

    public float getRate() {
        return rate;
    }

    public RestaurantDetailFragment() {
        // Required empty public constructor
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

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        navBar.setVisibility(v.GONE);

        return v;
    }



    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        name = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getName();
        address = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getAddress();
        rate = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getRate();
        img = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getImg();
        orderID = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getOrderID();



        TextView txt_restaurant_name = view.findViewById(R.id.txt_restaurant_name);
        TextView txt_address = view.findViewById(R.id.txt_address_detail);
        TextView txt_rate = view.findViewById(R.id.txt_rate_detail);
        ImageView img_detail = view.findViewById(R.id.img_restaurant_detail);

        txt_restaurant_name.setText(name);
        txt_address.setText(address);
        txt_rate.setText(String.valueOf(rate));
        Glide.with(getContext()).load(img).into(img_detail);

        id = RestaurantDetailFragmentArgs.fromBundle(getArguments()).getId();


        img_back = view.findViewById(R.id.img_res_detail_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
                navBar.setVisibility(getView().VISIBLE);
                navController.navigate(R.id.action_restaurantDetailFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = getView().findViewById(R.id.tablayout_res_detail);
        viewPager = getView().findViewById(R.id.viewpager_res_detail);


        //Add fragment vao tablayout
        adapter = new resdetailAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new detail_datdonFragment(),"Đặt đơn");
        adapter.AddFragment(new detail_danhgiaFragment(),"Đánh giá");
        //adapter.AddFragment(new detail_thongtinFragment(),"Thông tin");

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}