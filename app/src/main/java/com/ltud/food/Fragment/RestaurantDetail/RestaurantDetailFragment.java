package com.ltud.food.Fragment.RestaurantDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ltud.food.Model.Favourite;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Favourite.FavouriteViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantDetailFragment extends Fragment implements View.OnClickListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    resdetailAdapter adapter;
    ImageView img_back;
    FavouriteViewModel favouriteViewModel;
    private static boolean check_fav = false;
    String favID;
    ImageView img_favourite;
    NavController navController;

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
        return v;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

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
                navController.navigate(R.id.action_restaurantDetailFragment_to_homeFragment);
            }
        });

        img_favourite = view.findViewById(R.id.img_res_detail_fav);

        //Add a favourite
        img_favourite.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Get a favourite
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            favouriteViewModel = new ViewModelProvider(getActivity()).get(FavouriteViewModel.class);
            favouriteViewModel.getCurrentFavourite(id).observe(getViewLifecycleOwner(), new Observer<Favourite>() {
                @Override
                public void onChanged(Favourite favourite) {
                    check_fav = favourite.isCheck_fav();
                    favID = favourite.getId();
                    if (check_fav == true)
                    {
                        img_favourite.setImageResource(R.drawable.ic_favourite_bar);
                    }
                }
            });
        }
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
        adapter.AddFragment(new detail_thongtinFragment(),"Thông tin");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
            return;
        }

        if (check_fav == false)
        {
            Restaurant restaurant = new Restaurant(id, name, address, img, rate);
            favouriteViewModel.addOneFavourite(restaurant);
            img_favourite.setImageResource(R.drawable.ic_favourite_bar);
            Toast.makeText(getActivity(),"Quán đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }
        else{
            favouriteViewModel.deleteOneFavourite(favID);
            img_favourite.setImageResource(R.drawable.ic_favourite_outline);
            check_fav = false;
            Toast.makeText(getActivity(),"Quán đã bị xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }
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
        check_fav = false;
    }
}