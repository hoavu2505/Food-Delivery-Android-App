package com.ltud.food.Fragment.Login;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.MainActivity;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class loginFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText edtPhoneNumber, edtEmail, edtPassword;
    private Button btnPhoneSubmit, btnEmailSubmit;
    private TextView tvSignUpChange, tvPhoneLoginChange, tvEmailLoginChange;
    private FirebaseAuth auth;
    private NavController navController;
    private ViewGroup layoutPhoneLogin, layoutEmailLogin;
    private CustomProgressDialog progressDialog;
    private String email;
    private String password;

    public loginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtPhoneNumber = view.findViewById(R.id.edt_phone_number);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        btnPhoneSubmit = view.findViewById(R.id.btn_phone_submit);
        btnEmailSubmit = view.findViewById(R.id.btn_email_submit);
        tvSignUpChange = view.findViewById(R.id.tv_signUp);
        tvPhoneLoginChange = view.findViewById(R.id.tv_phone_login_change);
        tvEmailLoginChange = view.findViewById(R.id.tv_email_login_change);
        layoutPhoneLogin = (ViewGroup)view.findViewById(R.id.layout_phone);
        layoutEmailLogin = (ViewGroup)view.findViewById(R.id.layout_email);
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());

        // Check text input is not empty
        inputValidateSetUp();


        btnPhoneSubmit.setOnClickListener(this);
        btnEmailSubmit.setOnClickListener(this);
        tvSignUpChange.setOnClickListener(this::onClick);
        tvEmailLoginChange.setOnClickListener(this::onClick);
        tvPhoneLoginChange.setOnClickListener(this::onClick);
    }

    // if user login by phone number then check phone number entered
    private void inputValidateSetUp()
    {
        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Patterns.PHONE.matcher(s).matches())
                {
                    btnPhoneSubmit.setBackgroundColor(getResources().getColor(R.color.orange));
                    btnPhoneSubmit.setEnabled(true);
                }
                else
                {
                    btnPhoneSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                    btnPhoneSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // if user login by email then check if email and password is entered
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
                    btnEmailSubmit.setBackgroundColor(getResources().getColor(R.color.orange));
                    btnEmailSubmit.setEnabled(true);
                }
                else
                {
                    btnEmailSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                    btnEmailSubmit.setEnabled(false);
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
            case R.id.btn_phone_submit: verifyPhoneNumberAction(); break;
            case R.id.btn_email_submit: logInWithEmail(); break;
            case R.id.tv_signUp: navController.navigate(R.id.signUpEmailFragment); break;
            case R.id.tv_email_login_change: emailLoginChangeAction(); break;
            case R.id.tv_phone_login_change: phoneLoginChangeAction(); break;
        }
    }

    // Navigate to verify phone number fragment
    private void verifyPhoneNumberAction() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        NavDirections action = loginFragmentDirections.actionLoginFragmentToVerifyOTPFragment()
                .setPhoneNumber(phoneNumber);
        navController.navigate(action);
    }

    //log in
    private void logInWithEmail() {
        progressDialog.show();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            navController.navigate(R.id.userFragment);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Sai email hoặc mật khẩu", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // change to phone number log in
    private void emailLoginChangeAction() {
        layoutPhoneLogin.setVisibility(View.GONE);
        layoutEmailLogin.setVisibility(View.VISIBLE);
    }

    // change to email log in
    private void phoneLoginChangeAction() {
        layoutEmailLogin.setVisibility(View.GONE);
        layoutPhoneLogin.setVisibility(View.VISIBLE);
    }

}