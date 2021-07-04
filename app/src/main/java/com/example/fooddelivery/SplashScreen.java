package com.example.fooddelivery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    int waktu = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                LoginPage loginPage = new LoginPage();
                SharedPreferences sharedPreferences = getSharedPreferences(loginPage.SHARED_PREFS, Context.MODE_PRIVATE);

                if (sharedPreferences.getString(loginPage.SHARED_ROLE, "").equals("kurir")) {
                    Log.i("Info Login", "Kurir Login");
                    Intent intent = new Intent(SplashScreen.this, Kurir.class);
                    startActivity(intent);
                    finish();
                } else if(firebaseUser != null) {
                    Log.i("Info Login", "No Role Login");
                    Intent intent = new Intent(SplashScreen.this, LocationPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("Info Login", "User Login");
                    Intent home = new Intent(SplashScreen.this, MultiPage.class);
                    startActivity(home);
                    finish();
                }

            }
        }, waktu);
    }
}