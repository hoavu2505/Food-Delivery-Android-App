package com.ltud.food.Fragment.Customer;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.ltud.food.Adapter.GenderSpinnerAdapter;
import com.ltud.food.Dialog.CustomProgressDialog;
import com.ltud.food.Model.Customer;
import com.ltud.food.Model.Gender;
import com.ltud.food.R;
import com.ltud.food.ViewModel.Customer.CustomerViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class userFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String COLLECTION = "Customer";
    private static final int REQUEST_IMAGE_CODE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private static final int REQUEST_CAPTURE_IMAGE_CODE = 3;
    private CircleImageView imvAvatar;
    private ViewGroup frameName, frameAddress, frameBirthDay, frameGender;
    private TextView tvUser, tvName, tvAccount, tvAddress, tvBirthday;
    private Spinner spinGender;
    private List<Gender> genderList;
    private Button btnLogOut;
    private FirebaseAuth auth;
    private DocumentReference docRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private NavController navController;
    private CustomerViewModel customerViewModel;
    private Customer customer;
    private CustomProgressDialog progressDialog;
    private boolean isFirstChecked;

    public userFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();

        frameName = (ViewGroup) view.findViewById(R.id.frame_name);
        frameAddress = (ViewGroup) view.findViewById(R.id.frame_address);
        frameBirthDay = (ViewGroup) view.findViewById(R.id.frame_birthday);
        frameGender = (ViewGroup) view.findViewById(R.id.frame_gender);
        imvAvatar = (CircleImageView) view.findViewById(R.id.imv_avatar);
        tvUser = view.findViewById(R.id.tv_user);
        tvName = view.findViewById(R.id.tv_name);
        tvAccount= view.findViewById(R.id.tv_account);
        tvAddress = view.findViewById(R.id.tv_address);
        tvBirthday =  view.findViewById(R.id.tv_birthday);
        btnLogOut = (Button) view.findViewById(R.id.btn_log_out);

        //set up spinner
        spinGender = view.findViewById(R.id.spin_gender);
        genderList = Arrays.asList(new Gender("Không chọn"), new Gender("Nam"), new Gender("Nữ"));
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(getActivity(), R.layout.gender_layout, genderList);
        spinGender.setAdapter(adapter);
        spinGender.setOnItemSelectedListener(this);

        // event
        imvAvatar.setOnClickListener(this);
        frameName.setOnClickListener(this);
        frameAddress.setOnClickListener(this);
        frameBirthDay.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        isFirstChecked = true;
        storage = FirebaseStorage.getInstance();
        navController = Navigation.findNavController(view);
        customerViewModel = new ViewModelProvider(getActivity()).get(CustomerViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(auth.getCurrentUser() == null)
        {
            navController.navigate(R.id.loginFragment);
        }
        else
        {
            progressDialog = new CustomProgressDialog(getActivity());
            customerViewModel.getCustomerLiveData(user.getUid()).observe(getViewLifecycleOwner(), new Observer<Customer>() {
                @Override
                public void onChanged(Customer cus) {
                    customer = cus;
                    updateUI(customer);
                }
            });
            docRef = FirebaseFirestore.getInstance().collection(COLLECTION).document(user.getUid());
            updateCity();
        }
    }

    private void updateCity() {
        if(!userFragmentArgs.fromBundle(getArguments()).getCity().equals("null"))
        {
            String city = userFragmentArgs.fromBundle(getArguments()).getCity();
            docRef.update("address", city)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Địa chỉ được cập nhật", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void updateUI(Customer customer) {

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
            Glide.with(getContext())
                    .load(customer.getAvatar())
                    .centerCrop()
                    .into(imvAvatar);
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
            case R.id.frame_address: navController.navigate(R.id.autoCompleteLocationFragment); break;
            case R.id.frame_birthday: birthdayChange(); break;
            case R.id.btn_log_out: logOut(); break;
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
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NotNull String[] permissions,@NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getActivity(), "vao day", Toast.LENGTH_LONG).show();
        if(requestCode == REQUEST_PERMISSION_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE_CODE);
        }
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

        progressDialog.show();

        if(requestCode == REQUEST_IMAGE_CODE && data != null) {
            Uri avatarUri = data.getData();
            String filePath = "customer/" + avatarUri.getLastPathSegment();
            storageRef = storage.getReference().child(filePath);
            storageRef.putFile(avatarUri);

            storage.getReference().child(filePath).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            docRef.update("avatar", uri.toString());
                            imvAvatar.setImageURI(avatarUri);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NotNull Exception e) {
                            Log.d("error", e.getMessage());
                        }
                    });

        }

        else
        {
            Bitmap avatar = data.getParcelableExtra("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();
            String id = UUID.randomUUID().toString();
            String pathFile = "customer/" + id + ".jpg";
            storageRef = storage.getReference().child(pathFile);
            storageRef.putBytes(imageData);

            storage.getReference().child(pathFile).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            docRef.update("avatar", uri);
                            imvAvatar.setImageBitmap(avatar);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NotNull Exception e) {
                            Log.d("error", e.getMessage());
                        }
                    });
        }

        progressDialog.dismiss();
    }

    private void birthdayChange() {
        DialogFragment dialogFragment = new CustomerDatePickerFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "Birthday");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(isFirstChecked == true)
        {
            isFirstChecked = false;
        }
        else{
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void logOut() {
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();
        FirebaseAuth.getInstance().signOut();
        navController.navigate(R.id.loginFragment);
        progressDialog.dismiss();
    }
}
