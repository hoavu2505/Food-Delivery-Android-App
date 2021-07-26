package com.ltud.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class LocationPermissionActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialButton btnSubmit;
    MaterialButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);

        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSubmit)
        {
            Intent intent = new Intent(LocationPermissionActivity.this, CurrentLocationActivity.class);
            startActivity(intent);
        }
        else {
            finish();
        }
    }
}