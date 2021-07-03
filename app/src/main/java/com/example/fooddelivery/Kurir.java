package com.example.fooddelivery;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Kurir extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurir);
        textView = findViewById(R.id.judul_toolbar);
        recyclerView = findViewById(R.id.view_kurir);
    }
}
