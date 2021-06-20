package com.ltud.food.Fragment.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ltud.food.R;
import com.ltud.food.orderTabLayout.hientaiFragment;
import com.ltud.food.orderTabLayout.lichsuFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class orderFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    orderAdapter adapter;

    public orderFragment() {
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tabLayout = getView().findViewById(R.id.tablayout_order);
        viewPager = getView().findViewById(R.id.viewpager_order);


        //Add fragment vao tablayout
        adapter = new orderFragment.orderAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new hientaiFragment(),"Hiện tại");
        adapter.AddFragment(new lichsuFragment(),"Lịch sử");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


    }

    //Su dung homeAdapter der them cac fragment vao tablayout
    private class orderAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String>   stringArrayList   = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public orderAdapter(@NotNull FragmentManager fm) {
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
}