package com.example.satushyzerdeapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.modules.CheckInternetStatus;
import com.example.satushyzerdeapp.modules.Satushylar;
import com.example.satushyzerdeapp.modules.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

//    Button btnNameChanger, btnImageChanger, btnPasswordChanger, btnPhoneChanger, btnAddressChanger, btnEmailChanger;
//    ImageView imgView;
    TextView tvProfileName, tvProfilePhone, tvProfileEmail;
    Dialog dialog;
    FirebaseAuth mAuth;
    DatabaseReference mdatabaseReference;

    String email1;
    ProgressDialog progressDialog;
    String emailPath;

//    private Uri mImageUri;
//    private StorageReference mStorageRef;

    CheckInternetStatus mCheckInternetStatus;
    boolean is_internet_connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCheckInternetStatus = new CheckInternetStatus(ProfileActivity.this);
        is_internet_connected = mCheckInternetStatus.isInternetConnected();
        if (!is_internet_connected){
            Toast.makeText(this, "Интернетті тексеріңіз", Toast.LENGTH_LONG).show();
        }

        initViews();

//        btnNameChanger.setOnClickListener(this);
//        btnImageChanger.setOnClickListener(this);
//        btnAddressChanger.setOnClickListener(this);
//        btnEmailChanger.setOnClickListener(this);
//        btnPhoneChanger.setOnClickListener(this);
//        btnPasswordChanger.setOnClickListener(this);

//        imgView.setOnClickListener(this);

//        tvProfileName.setOnClickListener(this);
//        tvProfilePhone.setOnClickListener(this);
//        tvProfileEmail.setOnClickListener(this);
//        tvProfilePassword.setOnClickListener(this);
    }

    private void viewData() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");
        progressDialog.show();

        email1 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.i("email", "123 " + email1);
        emailPath = email1.replace(".", "-");
        Log.i("email", "123 " + emailPath);
        mdatabaseReference.child(emailPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Satushylar user = snapshot.getValue(Satushylar.class);
                    tvProfileName.setText(user.getNameSatushy() + " " + user.getSurnameSatushy());
                    tvProfilePhone.setText(user.getPhoneNumberSatushy());
                    tvProfileEmail.setText(user.getEmailSatushy());
                    progressDialog.cancel();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Бұл қолданушы туралы инфо жоқ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    private void initViews() {
//        btnNameChanger = findViewById(R.id.btnNameChanger);
//        btnEmailChanger = findViewById(R.id.btnEmailChanger);
//        btnPhoneChanger = findViewById(R.id.btnPhoneChanger);
//        btnPasswordChanger = findViewById(R.id.btnPasswordChanger);


        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        dialog = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Satushylar");
        //mStorageRef = FirebaseStorage.getInstance().getReference();

        viewData();
    }

//    private void dialogViewName() {
//        dialog.setContentView(R.layout.dialog_name);
//
//        EditText name, surname;
//        TextView btnOtmena;
//        Button btnSave;
//
//        name = dialog.findViewById(R.id.name);
//        surname = dialog.findViewById(R.id.surname);
//        btnOtmena = dialog.findViewById(R.id.btnOtmena);
//        btnSave = dialog.findViewById(R.id.btnSave);
//
//        btnOtmena.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(name.getText().toString()) || !TextUtils.isEmpty(surname.getText().toString())){
//                    mdatabaseReference.child(emailPath).child("fullName").setValue(name.getText().toString() + " " + surname.getText().toString());
//                    Toast.makeText(ProfileActivity.this, "Save btn clicked!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }else{
//                    Toast.makeText(ProfileActivity.this, "Толығымен толтырыңыз!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        dialog.show();
//    }

//    private void dialogViewEmail(){
//        dialog.setContentView(R.layout.dialog_name);
//
//        EditText name, surname;
//        TextView btnOtmena;
//        Button btnSave;
//
//        name = dialog.findViewById(R.id.name);
//        surname = dialog.findViewById(R.id.surname);
//        btnOtmena = dialog.findViewById(R.id.btnOtmena);
//        btnSave = dialog.findViewById(R.id.btnSave);
//
//        btnOtmena.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "Save btn clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        dialog.show();
//    }
//    private void dialogViewPhone() {
//        dialog.setContentView(R.layout.dialog_phone);
//
//        EditText phoneNumber;
//        TextView btnOtmena;
//        Button btnSave;
//
//        phoneNumber = dialog.findViewById(R.id.phoneNumber);
//        btnOtmena = dialog.findViewById(R.id.btnOtmena);
//        btnSave = dialog.findViewById(R.id.btnSave);
//
//        btnOtmena.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(phoneNumber.getText().toString())){
//                    mdatabaseReference.child("Users").child(emailPath).child("phone").setValue(phoneNumber.getText().toString());
//                    Toast.makeText(ProfileActivity.this, "Save btn clicked!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }else{
//                    Toast.makeText(ProfileActivity.this, "Толығымен толтырыңыз!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        dialog.show();
//    }

//    private void dialogViewPassword() {
//        dialog.setContentView(R.layout.dialog_password);
//
//        EditText emailChange;
//        TextView btnOtmena;
//        Button btnSave;
//
//        emailChange = dialog.findViewById(R.id.emailChange);
//        btnOtmena = dialog.findViewById(R.id.btnOtmena);
//        btnSave = dialog.findViewById(R.id.btnSend);
//
//        btnOtmena.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(emailChange.getText().toString())){
//                    mAuth.sendPasswordResetEmail(emailChange.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(ProfileActivity.this, "Reset message has been sent", Toast.LENGTH_SHORT).show();
//                            dialog.cancel();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Toast.makeText(ProfileActivity.this, "Save btn clicked!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }else {
//                    Toast.makeText(ProfileActivity.this, "Толығымен толтырыңыз!", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        dialog.show();
//    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnNameChanger:
//                //dialogViewName();
//                break;
//            case R.id.tvProfileName:
//                //dialogViewName();
//                break;
//            case R.id.btnEmailChanger:
//                //dialogViewEmail();
//                break;
//            case R.id.tvProfileEmail:
//                //dialogViewEmail();
//                break;
//            case R.id.btnPhoneChanger:
//                //dialogViewPhone();
//                break;
//            case R.id.tvProfilePhone:
//                //dialogViewPhone();
//                break;
//            case R.id.btnPasswordChanger:
//                //dialogViewPassword();
//                break;
//            case R.id.tvProfilePassword:
//                //dialogViewPassword();
//                break;
//        }
    }
}