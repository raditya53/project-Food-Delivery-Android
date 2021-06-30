package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailMenu extends AppCompatActivity {

    private TextView tvNama, tvDeskripsi, tvQuantity, tvHarga;
    private ImageButton increment,decrement;
    private ImageView imageView;
    private Button btnSubmit;

    private int Quantity;
    private String Uri,IdMenu,idCust, IdCartNow;
    private boolean isExist;

    private DatabaseReference databaseReference;
    private ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        databaseReference = FirebaseDatabase.getInstance().getReference("cart");

        tvNama = findViewById(R.id.namaDetail);
        tvDeskripsi = findViewById(R.id.deskripsiDetail);
        tvQuantity = findViewById(R.id.quantity);
        tvHarga = findViewById(R.id.hargaMenu);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        imageView = findViewById(R.id.imgDetail);
        btnSubmit = findViewById(R.id.submit);

        idCust = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        tvNama.setText(intent.getStringExtra("nama"));
        tvDeskripsi.setText(intent.getStringExtra("deskripsi"));
        tvHarga.setText(intent.getStringExtra("harga"));

        Uri = intent.getStringExtra("url");
        IdMenu = intent.getStringExtra("id");

        Picasso.with(this).load(Uri).fit().centerCrop().into(imageView);

        checkMenuInYourCart(IdMenu);

        if(isExist == false) {
            Quantity = 0;
            IdCartNow = String.valueOf(System.currentTimeMillis());
        }

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSubmit.getText().toString().equals("HAPUS")) {
                    btnSubmit.setText("SUBMIT");
                }
                Quantity++;
                tvQuantity.setText(String.valueOf(Quantity));
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Quantity == 0 ) {
                    Toast.makeText(DetailMenu.this, "Tidak Bisa Kurang Dari 0", Toast.LENGTH_SHORT).show();
                } else if(isExist == true && Quantity <= 1) {
                    btnSubmit.setText("HAPUS");
                    Quantity--;
                    tvQuantity.setText(String.valueOf(Quantity));
                } else {
                    Quantity--;
                    tvQuantity.setText(String.valueOf(Quantity));
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSubmit.getText().toString().equals("HAPUS")) {
                    deleteCart(IdCartNow);
                } else {
                    addToCart(IdMenu, IdCartNow);
                }
            }
        });
    }

    private void deleteCart(String idCartNow) {
        databaseReference.child(idCartNow).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailMenu.this, "Berhasil Menghapus Menu Dari Cart", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkMenuInYourCart(String idmenu) {
        listener = databaseReference.orderByChild("idMenu").equalTo(idmenu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   for (DataSnapshot ds : snapshot.getChildren()) {
                       DataCart dataCart = ds.getValue(DataCart.class);
                       if(dataCart.getIdCustomer().equals(idCust)) {
                           Quantity = Integer.parseInt(dataCart.getQuantityMenu());
                           tvQuantity.setText(dataCart.getQuantityMenu());
                           IdCartNow = dataCart.getIdCart();
                           isExist = true;
                       }
                   }
               } else {
                   isExist = false;
               }
               databaseReference.removeEventListener(listener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToCart(String idMenu, String idCart) {
        int harga = Integer.parseInt(tvQuantity.getText().toString()) * Integer.parseInt(tvHarga.getText().toString());
        DataCart cart = new DataCart(idCart, idMenu, idCust, tvNama.getText().toString(), tvQuantity.getText().toString(), String.valueOf(harga));
        databaseReference.child(idCart).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailMenu.this, "Barang Berhasil ditambah", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.child("cart").removeEventListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.child("cart").removeEventListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.child("cart").removeEventListener(listener);
    }
}