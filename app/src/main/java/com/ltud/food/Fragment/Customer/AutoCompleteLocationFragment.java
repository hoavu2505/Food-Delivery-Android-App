package com.ltud.food.Fragment.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

public class AutoCompleteLocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auto_complete_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.city));
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.tv_city);
        textView.setAdapter(adapter);
        boolean navigatePayment = AutoCompleteLocationFragmentArgs.fromBundle(getArguments()).getNavigatePayment();
        Log.i("log", String.valueOf(navigatePayment));
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                NavDirections action;
                if(navigatePayment == true)
                {
                    action = AutoCompleteLocationFragmentDirections.actionAutoCompleteLocationFragmentToCheckoutFragment()
                            .setAddress(textView.getText().toString());
                }
                else{
                    action = AutoCompleteLocationFragmentDirections.actionAutoCompleteLocationFragmentToUserFragment()
                            .setCity(textView.getText().toString());
                }
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
}