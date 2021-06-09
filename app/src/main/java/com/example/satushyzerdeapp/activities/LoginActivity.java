package com.example.satushyzerdeapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.satushyzerdeapp.MainActivity;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.modules.Satushylar;
import com.example.satushyzerdeapp.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailOrPhone, password;
    Button btnEnter;

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child("Satushylar");

        emailOrPhone = findViewById(R.id.etPhoneOrEmailLogin);
        password = findViewById(R.id.etPasswordLogin);
        btnEnter = findViewById(R.id.loginBtn);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailOrPhone.getText())) {
                    emailOrPhone.setError("Поле бос болмауы керек");
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Поле бос болмауы керек");
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Қолданушы ізделуде...");
                progressDialog.setMessage("Күте тұрыңыз");
                progressDialog.show();

                String sEmail = emailOrPhone.getText().toString();
                String sPassword = password.getText().toString();

                mDatabaseRef.orderByChild("emailSatushy").equalTo(emailOrPhone.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            mAuth.signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.cancel();
                                        Toast.makeText(getApplicationContext(), "Қолданушы табылды", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("email", sEmail);
                                        startActivity(intent);
                                    }else{
                                        progressDialog.cancel();
                                        Toast.makeText(getApplicationContext(), "Қолданушы табылмады", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            progressDialog.cancel();
                            Toast.makeText(LoginActivity.this, "Бұндай сатушы жоқ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}