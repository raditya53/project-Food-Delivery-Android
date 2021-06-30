package com.example.fooddelivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterAccount extends AppCompatActivity {
    private EditText etEmail, etPassword, etFullname, etPhoneNumber;
    private Button btnRegister;
    private TextView ToLoginPage;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.email);
        etFullname = findViewById(R.id.nama);
        etPassword = findViewById(R.id.password);
        etPhoneNumber = findViewById(R.id.nomorhp);
        ToLoginPage = findViewById(R.id.tologinpage);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister = findViewById(R.id.buttonsave);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        ToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser() {
        //validation
        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
        }

        if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
        }

        if(etFullname.getText().toString().isEmpty()){
            etFullname.setError("Fullname is required!");
            etFullname.requestFocus();
        }

        if(etPhoneNumber.getText().toString().isEmpty()){
            etPhoneNumber.setError("PhoneNumber is required!");
            etPhoneNumber.requestFocus();
        }

        //add new user
        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-android-19de2.appspot.com/userPicture/default_profile_picture.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                               if(task.isSuccessful()) {
                                   DataUser user = new DataUser(id, etFullname.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etPhoneNumber.getText().toString(),uri.toString());

                                   //new entry to database user
                                   firebaseDatabase.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()) {
                                               etFullname.setText("");
                                               etEmail.setText("");
                                               etPassword.setText("");
                                               etPhoneNumber.setText("");
                                               FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                                               Toast.makeText(RegisterAccount.this, "Data Registerd! Please Check Your Email", Toast.LENGTH_SHORT).show();

                                               Intent intent = new Intent(RegisterAccount.this, LoginPage.class);
                                               startActivity(intent);
                                           } else {
                                               Toast.makeText(RegisterAccount.this, "Something Wrong Happened!, Please Try Again", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(RegisterAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   });
                               }
                           }
                       });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
