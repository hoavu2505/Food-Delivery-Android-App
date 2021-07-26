package com.ltud.food.Fragment.Order.HistoryTab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.DatePicker;

import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public class HistoryDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public HistoryDatePickerFragment(onSetedDate setedDate)
    {
        this.date = setedDate;
    }

    onSetedDate date;
    public interface onSetedDate
    {
        void onSet(Date date);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.DialogTheme,this,year,month,date);

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date myDate = calendar.getTime();
        date.onSet(myDate);
        Log.i("log", String.valueOf(myDate));
    }
}