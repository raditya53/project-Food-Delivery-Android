package com.example.fooddelivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Kurir extends AppCompatActivity {
    private TextView textView;
    private ImageButton imageButton;
    private RecyclerView recyclerView;
    private List<DataHistory> dataHistoryList;
    private List<DataUser> dataUserList;
    private String deliverStatus;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    KurirAdapter kurirAdapter;
    public static final String SHARED_PREFS = "roleAccount";
    public static final String SHARED_ROLE = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurir);
        textView = findViewById(R.id.judul_toolbar);
        recyclerView = findViewById(R.id.view_kurir);
        imageButton = findViewById(R.id.logoutkurir);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Kurir.this));
        dataHistoryList = new ArrayList<>();
        dataUserList = new ArrayList<>();
        showAllData();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutKurir();
            }
        });
    }

    public void location(String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open Map");
        builder.setMessage("Open Map For Address:\n" + address);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutKurir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Logout");
        builder.setMessage("Logout From Kurir Account?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(SHARED_ROLE, "No User");
                editor.apply();
                dialog.dismiss();

                Intent intent = new Intent(Kurir.this, MultiPage.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Kurir.this, "Cancel Logout", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAllData(){
        databaseReference.child("transaction").orderByChild("deliverStatus").equalTo("Shipping").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    dataHistoryList.clear();
                    dataUserList.clear();
                    for(DataSnapshot item : snapshot.getChildren()) {
                        DataHistory dataHistory = item.getValue(DataHistory.class);
                        dataHistoryList.add(dataHistory);
                        getUserData(dataHistory.getIdCustomer());
                    }
                } else {
                    dataHistoryList.clear();
                    dataUserList.clear();
                    callRecycler();
                    Log.i("Data Kosong", "Tidak ada transaksi yang diantar");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData(String idCustomer) {
        databaseReference.child("Users").orderByChild("idUser").equalTo(idCustomer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot item : snapshot.getChildren()) {
                        DataUser dataUser = item.getValue(DataUser.class);
                        dataUserList.add(dataUser);
                    }

                    if(dataUserList.size() == dataHistoryList.size()) {
                        callRecycler();
                    }

                } else {
                    Log.i("Data User", "Tidak ada user");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void callRecycler() {
        Log.i("Recycler", "recycler called");
        recyclerView.getRecycledViewPool().clear();
        kurirAdapter = new KurirAdapter(Kurir.this, dataHistoryList, dataUserList);
        kurirAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(kurirAdapter);
    }

    public void changeStatusDeliver(String idTransaksi, String status) {

        if(status.equals("Success")) {
            deliverStatus = "Delivered";
        } else if(status.equals("Failed")) {
            deliverStatus = "Failed";
        }

        databaseReference.child("transaction/"+idTransaksi).child("deliverStatus").setValue(deliverStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Kurir.this, "Transaction Set To Delivered", Toast.LENGTH_SHORT).show();
                showAllData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Kurir.this, "Change Value Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DialogKurir(String idTransaksi, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Status Delivery");
        builder.setMessage("Set Deliver Status To " + status + " ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeStatusDeliver(idTransaksi, status);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Kurir.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllData();
    }
}
