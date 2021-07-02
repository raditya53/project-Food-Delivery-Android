package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class DetailHistory extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView status,tanggal,idTransaksi;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailhistory);
        imageButton = findViewById(R.id.backward);
        status = findViewById(R.id.status);
        tanggal = findViewById(R.id.tanggalhistorydetail);
        idTransaksi = findViewById(R.id.idtransaksidetail);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHistory.this, NavigasiTab.class);
                startActivity(intent);
            }
        });
    }
}
