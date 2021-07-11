package com.ltud.food.Fragment.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.material.tabs.TabLayout;
import com.ltud.food.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class homeFragment extends Fragment{

    ImageView img_search, img_com, img_douong, img_anvat;

    TabLayout tabLayout;
    ViewPager viewPager;
    homeAdapter adapter;


    public homeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        NavController navController = Navigation.findNavController(view);


        img_search = view.findViewById(R.id.img_search);

        img_com = view.findViewById(R.id.img_com);
        img_douong = view.findViewById(R.id.img_douong);
        img_anvat = view.findViewById(R.id.img_anvat);

        //Dieu huong sang fragment search
        img_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_searchFragment);
                onDestroyView();
            }
        });

        img_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_com.setImageResource(R.drawable.com_picked);
                img_douong.setImageResource(R.drawable.douong);
                img_anvat.setImageResource(R.drawable.anvat);
            }
        });

        img_douong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_com.setImageResource(R.drawable.com);
                img_douong.setImageResource(R.drawable.douong_picked);
                img_anvat.setImageResource(R.drawable.anvat);
            }
        });

        img_anvat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_com.setImageResource(R.drawable.com);
                img_douong.setImageResource(R.drawable.douong);
                img_anvat.setImageResource(R.drawable.anvat_picked);
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);



        tabLayout = getView().findViewById(R.id.tablayout_home);
        viewPager = getView().findViewById(R.id.viewpager_home);


        //Add fragment vao tablayout
        adapter = new homeAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new gantoiFragment(),"Gần tôi");
        adapter.AddFragment(new banchayFragment(),"Bán chạy");
        adapter.AddFragment(new danhgiaFragment(),"Đánh giá");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


    }

    //Su dung homeAdapter de them cac fragment vao tablayout
    private class homeAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String>   stringArrayList   = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public homeAdapter(@NotNull FragmentManager fm) {
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