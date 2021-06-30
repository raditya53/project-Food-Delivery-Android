package com.example.fooddelivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CheckoutFragment extends Fragment  {

    private RecyclerView recyclerView;
    private List<DataCart> dataCartList;
    private TextView totalHarga,location;
    private Button btnCheckout;

    private String userUID;
    private DatabaseReference databaseReference, databaseReference1;

    private CheckoutAdapter adapter;
    private int total = 0;
    private ValueEventListener listener;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Maps = view.findViewById(R.id.locationButton);
//        etLocation = view.findViewById(R.id.search_cart);
        totalHarga = view.findViewById(R.id.totalharga);
        btnCheckout = view.findViewById(R.id.button_checkout);
        recyclerView = view.findViewById(R.id.view_cart);
        location = view.findViewById(R.id.location);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


//        Intent intent = getActivity().getIntent();
//        location.setText(intent.getStringExtra("address"));
        
        locationload();


        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("cart");

        dataCartList = new ArrayList<>();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutMenu();
            }
        });

    }

    private void locationload() {
        LocationPage locationPage = new LocationPage();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(locationPage.SHARED_PREFS, Context.MODE_PRIVATE);
        location.setText(sharedPreferences.getString(locationPage.SHARED_LOCATION,""));

    }

    private void checkoutMenu() {
        databaseReference.orderByChild("idCustomer").equalTo(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idTransaksi = String.valueOf(System.currentTimeMillis());
                Intent intent = new Intent(getContext(), PaymentPage.class);
                intent.putExtra("location", location.getText().toString());
                intent.putExtra("idTransaksi", idTransaksi);
                intent.putExtra("totalHarga", total);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAllCart() {
       listener =  databaseReference.orderByChild("idCustomer").equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {

                    DataCart dataCart = item.getValue(DataCart.class);
                    dataCartList.add(dataCart);
                    total = total + Integer.parseInt(dataCart.getHargaMenu());
                }
                totalHarga.setText(String.valueOf(total));
                databaseReference.removeEventListener(listener);
                adapter = new CheckoutAdapter(getContext(), dataCartList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        total = 0;
        dataCartList.clear();
        showAllCart();
    }
}