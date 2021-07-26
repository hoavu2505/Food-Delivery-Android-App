package com.ltud.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if(destination.getId() != R.id.homeFragment && destination.getId() != R.id.orderFragment && destination.getId() != R.id.notiFragment
                        && destination.getId() != R.id.favFragment && destination.getId() != R.id.userFragment)
                {
                    bottomNavigationView.setVisibility(View.GONE);
                }
                else
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }
}
