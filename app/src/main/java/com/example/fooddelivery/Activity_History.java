package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Activity_History extends AppCompatActivity {
    ImageButton back;
    private List<DataHistory> dataHistoryList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String userUID;
    HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;
    private List<DataMenu> dataMenu;
    List<DataMenu> data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back = findViewById(R.id.backward);
        recyclerView = findViewById(R.id.recycle_view);
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_History.this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_History.this, NavigasiTab.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        showHistoryData();
    }

    private void showHistoryData() {
        databaseReference.child("transaction").orderByChild("idCustomer").equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataHistoryList = new ArrayList<>();
                    data = new ArrayList<>();
                    dataMenu = new ArrayList<>();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        DataHistory dataHistory = item.getValue(DataHistory.class);
                        getDataMenu(dataHistory.getIdCart());
                        dataHistoryList.add(dataHistory);
                    }

                } else {
                    Log.w("Error", "Data Tidak ada");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataMenu(Map<String, Object> idCart) {
        Object[] idValue = idCart.values().toArray();

        databaseReference.child("history").child(idValue[0].toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReference.child("data-barang").orderByChild("id").equalTo(snapshot.child("idMenu").getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot item : snapshot.getChildren()) {
                                DataMenu dm = item.getValue(DataMenu.class);
                                dataMenu.add(dm);
                            }

                            if(dataMenu.size() == dataHistoryList.size()) {
                                callRecycler();
                            }
//                            Log.i("Nilai Menu", String.valueOf(dataMenu.size()));
//                            Log.i("Nilai History", String.valueOf(dataHistoryList.size()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Log.i("Error :", "Snapshot Not FOund");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void callRecycler() {
        historyAdapter = new HistoryAdapter(Activity_History.this, dataHistoryList, dataMenu);
        recyclerView.setAdapter(historyAdapter);
    }
}
