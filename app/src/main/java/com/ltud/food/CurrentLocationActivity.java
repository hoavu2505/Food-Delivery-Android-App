package com.ltud.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.ltud.food.Dialog.CustomProgressDialog;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class CurrentLocationActivity extends AppCompatActivity{

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 44 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation();
        }
        else {
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    private String getLocation(LatLng latLng) {
        String address = "";
        Geocoder geocoder = new Geocoder(CurrentLocationActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            if(address != null)
            {
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder("");

                for(int i=0; i<=returnAddress.getMaxAddressLineIndex(); i++)
                {
                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");
                }

                address = stringBuilder.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return address;
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title(getLocation(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.addMarker(options);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setLocation(getLocation(latLng));
                                }
                            }, 2100);
                        }
                    });
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLocationActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Làm ơn hãy kiểm tra chắc chắn GPS đã được bật và tiếp tục !")
                            .setTitle("Thông báo");
                    builder.setPositiveButton("Đã bật", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void setLocation(String currentLocation) {
        SharedPreferences sharedPref = getSharedPreferences("locationPreference", CurrentLocationActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPref.edit();
        Intent intent = new Intent(CurrentLocationActivity.this, MainActivity.class);
        if(!sharedPref.contains("location"))
        {
            editor.putString("location", currentLocation);
            editor.commit();
            startActivity(intent);
        }
        else {
            String lastLocation = sharedPref.getString("location", "none");
            if(!lastLocation.trim().equals(currentLocation.trim()))
            {
                Dialog dialog = new Dialog(CurrentLocationActivity.this);
                dialog.setContentView(R.layout.change_location_dialog);
                dialog.show();

                TextView tvAddress = dialog.findViewById(R.id.tv_dia_chi);
                tvAddress.setText(currentLocation);
                MaterialButton btnSubmit = dialog.findViewById(R.id.btn_submit);
                MaterialButton btnCancel = dialog.findViewById(R.id.btn_cancel);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("location", currentLocation);
                        editor.commit();
                        startActivity(intent);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                });
            }
            else {
                startActivity(intent);
            }
        }
    }

}