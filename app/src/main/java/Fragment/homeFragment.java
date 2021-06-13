package Fragment;

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
import homeTabLayout.banchayFragment;
import homeTabLayout.danhgiaFragment;
import homeTabLayout.gantoiFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class homeFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tabLayout = getView().findViewById(R.id.tablayout_home);
        viewPager = getView().findViewById(R.id.viewpager_home);

        adapter = new homeAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new gantoiFragment(),"Gần tôi");
        adapter.AddFragment(new banchayFragment(),"Bán chạy");
        adapter.AddFragment(new danhgiaFragment(),"Đánh giá");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


    }

    private class homeAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String>   stringArrayList   = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public homeAdapter(@NonNull @NotNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }

}