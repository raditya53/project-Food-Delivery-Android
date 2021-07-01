package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

public class DetailHistory extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView status,tanggal,idTransaksi;
    private RecyclerView recycler;
    HistoryDetailAdapter historyDetailAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String userUID;
    private List<DataCart> listDataCart;
    private List<DataMenu> listDataMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailhistory);
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageButton = findViewById(R.id.backward);

        recycler = findViewById(R.id.recycle_view_detail);
        recycler.setLayoutManager(new LinearLayoutManager(DetailHistory.this));
        recycler.setAdapter(historyDetailAdapter);

        status = findViewById(R.id.status);
        tanggal = findViewById(R.id.tanggalhistorydetail);
        idTransaksi = findViewById(R.id.idtransaksidetail);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHistory.this, Activity_History.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        idTransaksi.setText(intent.getStringExtra("idTransaksi"));
        status.setText(intent.getStringExtra("statusDeliver"));
        tanggal.setText(intent.getStringExtra("tanggalPembelian"));

        showMenuDetail();
    }

    public void showMenuDetail() {
        String idtransaksi = idTransaksi.getText().toString();

        databaseReference.child("transaction").orderByChild("idTransaksi").equalTo(idtransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        DataHistory dataHistory = item.getValue(DataHistory.class);
                        getCartMenu(dataHistory.getIdCart());
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

    private void getCartMenu(Map<String, Object> idCart) {
        Object[] idValue = idCart.values().toArray();
        listDataCart = new ArrayList<>();
        listDataMenu = new ArrayList<>();

        for(int i = 0; i < idValue.length; i++) {
            databaseReference.child("history").child(idValue[i].toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        Log.i("data Cart", snapshot.getValue().toString());
                        Log.i("cek id", snapshot.child("idMenu").getValue().toString());
                        DataCart dataCart = snapshot.getValue(DataCart.class);
                        listDataCart.add(dataCart);

                        databaseReference.child("data-barang").orderByChild("id").equalTo(snapshot.child("idMenu").getValue().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot item : snapshot.getChildren()) {
                                    DataMenu dm = item.getValue(DataMenu.class);
                                    listDataMenu.add(dm);
                                }

                                Log.i("Total IdCart", String.valueOf(listDataCart.size()));
                                Log.i("Total IdMenu", String.valueOf(listDataMenu.size()));

                                if(listDataCart.size() == listDataMenu.size()) {
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
    }

    private void callRecycler() {
        Log.i("Check:" ," Calling  Recycler");
        historyDetailAdapter = new HistoryDetailAdapter(DetailHistory.this, listDataCart, listDataMenu);
        recycler.setAdapter(historyDetailAdapter);
    }
}