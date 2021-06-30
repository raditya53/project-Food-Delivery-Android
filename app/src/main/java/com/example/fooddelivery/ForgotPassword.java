package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    ImageButton imageButton;
    EditText emailField;
    Button resetPass;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        imageButton = findViewById(R.id.back);
        emailField = findViewById(R.id.email);
        resetPass = findViewById(R.id.btn_resetpass);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void resetPassword() {
        if(emailField.getText().toString().isEmpty()){
            emailField.setError("Email is required!");
            emailField.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString().trim()).matches()) {
            emailField.setError("Please provide valid email!");
            emailField.requestFocus();
        }

        firebaseAuth.sendPasswordResetEmail(emailField.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    emailField.setText("");
                    Toast.makeText(ForgotPassword.this, "Please Check Your Email To Reset Your Password!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, "Sorry, We Didn't Recognize Your Email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
