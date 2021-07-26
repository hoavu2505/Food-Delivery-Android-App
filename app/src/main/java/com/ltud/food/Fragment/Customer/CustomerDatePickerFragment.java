package com.ltud.food.Fragment.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ltud.food.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomerDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public CustomerDatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_DARK,this,year,month,date);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar =Calendar.getInstance();
        calendar.set(Calendar.DATE, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("Customer")
                .document(user.getUid());
        docRef.update("birthday", date);
        ((TextView) getActivity().findViewById(R.id.tv_birthday)).setText(date);
    }
}