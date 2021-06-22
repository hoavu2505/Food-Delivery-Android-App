package com.ltud.food.Fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.MainActivity;
import com.ltud.food.Model.Customer;
import com.ltud.food.R;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class verifyOTPFragment extends Fragment implements View.OnClickListener {

    private EditText edtOTP1, edtOTP2, edtOTP3, edtOTP4, edtOTP5, edtOTP6;
    private TextView tvResendOTP;
    private Button btnVerify;
    private CustomProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private NavController navController;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String phoneNumber;
    private String verifyCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseFirestore db;

    public verifyOTPFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtOTP1 = view.findViewById(R.id.tv_otp_1);
        edtOTP2 = view.findViewById(R.id.tv_otp_2);
        edtOTP3 = view.findViewById(R.id.tv_otp_3);
        edtOTP4 = view.findViewById(R.id.tv_otp_4);
        edtOTP5 = view.findViewById(R.id.tv_otp_5);
        edtOTP6 = view.findViewById(R.id.tv_otp_6);
        setUpOTPInput();
        tvResendOTP = view.findViewById(R.id.tv_resend_otp);
        btnVerify = view.findViewById(R.id.btn_verify);

        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        phoneNumber = verifyOTPFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        verifyPhoneNumber();

        progressDialog = new CustomProgressDialog(getActivity());
        db = FirebaseFirestore.getInstance();
;
        btnVerify.setOnClickListener(this);
        tvResendOTP.setOnClickListener(this::onClick);
    }

    // Enter OTP
    private void setUpOTPInput() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                {
                    btnVerify.setEnabled(true);
                    btnVerify.setBackgroundColor(getResources().getColor(R.color.background_button));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtOTP1.addTextChangedListener(textWatcher);
        edtOTP2.addTextChangedListener(textWatcher);
        edtOTP3.addTextChangedListener(textWatcher);
        edtOTP4.addTextChangedListener(textWatcher);
        edtOTP5.addTextChangedListener(textWatcher);
        edtOTP6.addTextChangedListener(textWatcher);

        edtOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                    edtOTP2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                    edtOTP3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                    edtOTP4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                    edtOTP5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                    edtOTP6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Verify phone number
    private void verifyPhoneNumber() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                signInProcess(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                Toast.makeText(getActivity(), "Số điện thoại không tồn tại", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verifyCode = s;
                resendingToken = forceResendingToken;
            }
        };

        // Send code verification
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInProcess(PhoneAuthCredential phoneAuthCredential) {
        String code = phoneAuthCredential.getSmsCode();
        edtOTP1.setText(String.valueOf(code.charAt(0)));
        edtOTP2.setText(String.valueOf(code.charAt(1)));
        edtOTP3.setText(String.valueOf(code.charAt(2)));
        edtOTP4.setText(String.valueOf(code.charAt(3)));
        edtOTP5.setText(String.valueOf(code.charAt(4)));
        edtOTP6.setText(String.valueOf(code.charAt(5)));
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_verify: checkSmsCode(); break;
            case R.id.tv_resend_otp: resendOTP(); break;
        }
    }

    private void checkSmsCode() {
        if(verifyCode == null)
            return;

        btnVerify.setEnabled(false);
        String otpCode = edtOTP1.getText().toString() + edtOTP2.getText().toString() + edtOTP3.getText().toString()
                + edtOTP4.getText().toString() + edtOTP5.getText().toString() + edtOTP6.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendOTP() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(callbacks)
                .setForceResendingToken(resendingToken)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        Toast.makeText(getActivity(), "Đã gửi", Toast.LENGTH_LONG).show();
    }

    // Sign in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        progressDialog.show();

        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            db.collection("Customer").document(user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(!documentSnapshot.exists())
                                            {
                                                Customer customer = new Customer(user.getUid(), user.getPhoneNumber(), "", "", "", "none", "");
                                                FirebaseFirestore.getInstance().collection("Customer").document(user.getUid())
                                                        .set(customer);
                                            }
                                            else {
                                                Log.i("exist", "Exists");
                                            }
                                        }
                                    });

                            navController.navigate(R.id.homeFragment);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Mã OTP không hợp lệ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}