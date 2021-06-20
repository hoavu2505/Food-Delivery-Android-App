package com.ltud.food.Fragment.user;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ltud.food.Adapter.GenderSpinnerAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Fragment.DatePickerFragment;
import com.ltud.food.Model.Customer;
import com.ltud.food.Model.Gender;
import com.ltud.food.R;
import com.ltud.food.ViewModel.CustomerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class userFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String COLLECTION = "Customer";
    private final int REQUEST_IMAGE_CODE = 1;
    private CircleImageView imvAvatar;
    private ViewGroup frameName, frameAddress, frameBirthDay, frameGender;
    private TextView  tvUser, tvName, tvAccount, tvAddress, tvBirthday;
    private Spinner spinGender;
    private List<Gender> genderList;
    private Button btnLogOut;
    private FirebaseAuth auth;  //Check state
    private DocumentReference docRef;
    private NavController navController;
    private CustomerViewModel customerViewModel;
    private String customerID;
    private CustomProgressDialog progressDialog;
    private Customer customer;

    public userFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState != null)
            customer = (Customer) savedInstanceState.getSerializable("customer");

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("customer", customer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameName = (ViewGroup) view.findViewById(R.id.frame_name);
        frameAddress = (ViewGroup) view.findViewById(R.id.frame_address);
        frameBirthDay = (ViewGroup) view.findViewById(R.id.frame_birthday);
        frameGender = (ViewGroup) view.findViewById(R.id.frame_gender);
        imvAvatar = (CircleImageView) view.findViewById(R.id.imv_avatar);
        tvUser = (TextView) view.findViewById(R.id.tv_user);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvAccount= (TextView) view.findViewById(R.id.tv_account);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvBirthday = (TextView) view.findViewById(R.id.tv_birthday);
        btnLogOut = (Button) view.findViewById(R.id.btn_log_out);

        //set up spin
        spinGender = view.findViewById(R.id.spin_gender);
        genderList = Arrays.asList(new Gender("Không chọn"), new Gender("Nam"), new Gender("Nữ"));
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(getActivity(), R.layout.gender_layout, genderList);
        spinGender.setAdapter(adapter);

        customerID = userFragmentArgs.fromBundle(getArguments()).getUserID();
        auth = FirebaseAuth.getInstance();
        docRef = FirebaseFirestore.getInstance().collection(COLLECTION).document(customerID);
        navController = Navigation.findNavController(view);
        progressDialog = new CustomProgressDialog(getActivity());

        // event
        imvAvatar.setOnClickListener(this);
        frameName.setOnClickListener(this);
        frameAddress.setOnClickListener(this);
        frameBirthDay.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        // change gender
        spinGender.setOnItemSelectedListener(this);

        customerViewModel = new ViewModelProvider(getActivity()).get(CustomerViewModel.class);
        customerViewModel.getCustomerLiveData(customerID).observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer cus) {
                customer = cus;
                updateUI();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //state
        if(auth.getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
        }
        else{
            customerViewModel.getCustomerLiveData(user.getUid()).observe(getViewLifecycleOwner(), new Observer<Customer>() {
                @Override
                public void onChanged(Customer cus) {
                    customer = cus;
//                    updateUI(customer);
                }
            });
            docRef = FirebaseFirestore.getInstance().collection(COLLECTION).document(customerID);
        }
    }

    private void updateUI() {
        progressDialog.show();

        if(customer.getAvatar().isEmpty()) {
            switch (customer.getGender()) {
                case "Nam":
                    imvAvatar.setImageResource(R.drawable.avatar_male);
                    break;
                case "Nữ":
                    imvAvatar.setImageResource(R.drawable.avatar_female);
                    break;
                default:
                    imvAvatar.setImageResource(R.drawable.avatar_anonymous);
                    break;
            }
        }
        else
        {
            imvAvatar.setImageURI(Uri.parse(customer.getAvatar()));
        }
        tvUser.setText(customer.getName().isEmpty() ? "User":customer.getName());
        tvName.setText(customer.getName());
        tvAccount.setText(customer.getAccount());
        tvAddress.setText(customer.getAddress());
        tvBirthday.setText(String.valueOf(customer.getBirthday()));

        if(customer.getGender().equals("Nam"))
            spinGender.setSelection(1);
        else if(customer.getGender().equals("Nữ"))
            spinGender.setSelection(2);
        else spinGender.setSelection(0);

        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_avatar: avatarChange(); break;
            case R.id.frame_name: nameChange(); break;
            case R.id.frame_address: addressChange(); break;
            case R.id.frame_birthday: birthdayChange(); break;
            case R.id.btn_log_out: logOut(); break;
        }
    }

    private void avatarChange() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pick_image_layout);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        ViewGroup camera = (ViewGroup) dialog.findViewById(R.id.camera);
        ViewGroup gallery = (ViewGroup) dialog.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pickImageCamera();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pickImageGallery();
            }
        });

    }

    private void pickImageCamera() {
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CODE && data != null)
        {
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            if(!customer.getAvatar().isEmpty())
            {
                StorageReference storageRef = storage.getReference().child("customer/" + customer.getAvatar());
                storageRef.delete();
            }

            Uri avatarUri = data.getData();
            StorageReference storageRef = storage.getReference().child("customer/" + avatarUri.getLastPathSegment());
            storageRef.putFile(avatarUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                // update image in database
                                docRef.update("avatar", String.valueOf(avatarUri))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                imvAvatar.setImageURI(avatarUri);
                                                Toast.makeText(getContext(), "Avatar đã được thay đổi", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void nameChange() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.change_name_dialog_layout);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        ImageView imvBack = imvBack = (ImageView) dialog.findViewById(R.id.imv_back);
        EditText edtChangeName = (EditText) dialog.findViewById(R.id.edt_Change_Name);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_save);
        edtChangeName.setText(tvName.getText().toString());
        edtChangeName.requestFocus();

        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.update("name", edtChangeName.getText().toString());
                dialog.dismiss();
                tvUser.setText(edtChangeName.getText().toString());
                tvName.setText(edtChangeName.getText().toString());
                Toast.makeText(getActivity(), "Tên được sửa", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addressChange() {
    }

    private void birthdayChange() {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "Birthday");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvBirthday.setText(String.valueOf(documentSnapshot.get("birthday")));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinGender.setSelection(position);
        docRef.update("gender", genderList.get(position).getGender())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(customer.getAvatar().isEmpty())
                        {
                            switch (position)
                            {
                                case 0 : imvAvatar.setImageResource(R.drawable.avatar_anonymous); break;
                                case 1 : imvAvatar.setImageResource(R.drawable.avatar_male); break;
                                case 2 : imvAvatar.setImageResource(R.drawable.avatar_female); break;
                            }
                        }
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        navController.navigate(R.id.loginFragment);
    }
}