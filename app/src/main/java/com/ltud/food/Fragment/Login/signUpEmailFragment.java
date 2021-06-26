package com.ltud.food.Fragment.Login;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Customer;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class signUpEmailFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText edtEmail, edtPassword;
    private Button btnSignUp;
    private ImageButton imbBack;
    private NavController navController;
    private String email;
    private String password;
    private CustomProgressDialog progressDialog;
    private FirebaseFirestore db;

    public signUpEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imbBack = (ImageButton) view.findViewById(R.id.imb_back);
        edtEmail = (TextInputEditText) view.findViewById(R.id.edt_email);
        edtPassword = (TextInputEditText) view.findViewById(R.id.edt_password);
        btnSignUp = (Button) view.findViewById(R.id.btn_email_signUp);
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());
        db = FirebaseFirestore.getInstance();

        inputValidate();

        imbBack.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void inputValidate() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 5)
                {
                    btnSignUp.setBackgroundColor(getResources().getColor(R.color.background_button));
                    btnSignUp.setEnabled(true);
                }
                else
                {
                    btnSignUp.setBackgroundColor(getResources().getColor(R.color.grey));
                    btnSignUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtEmail.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imb_back: navController.navigate(R.id.loginFragment); break;
            case R.id.btn_email_signUp: signUpEmail(); break;
        }
    }

    private void signUpEmail() {
        progressDialog.show();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();
                            Customer customer = new Customer(user.getUid(), user.getEmail(), "", "", "", "none", "");
                            FirebaseFirestore.getInstance().collection("Customer").document(user.getUid())
                                    .set(customer);
                            navController.navigate(R.id.homeFragment);
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}