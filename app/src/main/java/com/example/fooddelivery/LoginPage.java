package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    private Button login;
    private TextView ToSignUp,forgotpass;
    private EditText etEmail, etPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.Button_start);
        ToSignUp = findViewById(R.id.signup);
        etEmail = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        forgotpass = findViewById(R.id.forgotpassword);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        ToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterAccount.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });


    }

    private void loginUser() {
        if(etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Please Provide Valid Email!");
            etEmail.requestFocus();
        }

        if(etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
        }

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(firebaseUser.isEmailVerified()) {
                                Intent intent = new Intent(LoginPage.this, LocationPage.class);
                                startActivity(intent);
                            } else {
                                AlertDialog builder = new AlertDialog.Builder(LoginPage.this)
                                        .setTitle("Account Unverified")
                                        .setMessage("Your Account is Unverified,\nPlease Check Your Email \nOr Send New Verification?")
                                        .setPositiveButton("Yes", null)
                                        .setNegativeButton("No", null)
                                        .show();
                                Button positiveBtn = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(LoginPage.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                        builder.dismiss();
                                    }
                                });
                                Button negativBtn = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                                negativBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(LoginPage.this, "Please Check Your Email To Verify Your Account!", Toast.LENGTH_SHORT).show();
                                        builder.dismiss();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginPage.this, "Login Failed! Please Check Your Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
