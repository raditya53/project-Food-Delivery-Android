package com.example.fooddelivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class CheckoutFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<DataCart> dataCartList;
    private List<DataMenu> dataMenuList;
    private TextView totalHarga, location;
    private Button btnCheckout;

    private Dialog dialog;
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
        totalHarga.setText("0");

//        Intent intent = getActivity().getIntent();
//        location.setText(intent.getStringExtra("address"));

        locationload();

        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        Handler h = new Handler();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_dialog_checkout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.text);
        GifImageView gifImage = dialog.findViewById(R.id.image);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    h.postDelayed(new Runnable() {
                        int i = 0;
                        @Override
                        public void run() {
                            textView.setText("Gathering Information...(" + i + "/9)");
                            i += 1;
                            h.postDelayed(this, 1000);
                            if (i > 9) {
                                h.postDelayed(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void run() {
                                        gifImage.setVisibility(View.GONE);
                                        textView.setText("Payment Accepted ✔️");
                                        checkoutMenu();
                                        h.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.i("dialog status", "dialog dismiss");
                                                totalHarga.setText("0");
                                                h.removeCallbacksAndMessages(null);
                                                dialog.hide();
                                            }
                                        }, 2000);
                                    }
                                }, 1000);
                            }
                        }
                    }, 2000);
                }
            }
        }, 1000);
    }

    private void locationload() {
        LocationPage locationPage = new LocationPage();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(locationPage.SHARED_PREFS, Context.MODE_PRIVATE);
        location.setText(sharedPreferences.getString(locationPage.SHARED_LOCATION, ""));

    }

    private void showAllCart() {
        listener = databaseReference.child("cart").orderByChild("idCustomer").equalTo(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataCartList = new ArrayList<>();
                    dataMenuList = new ArrayList<>();
                    total = 0;
                    for (DataSnapshot item : snapshot.getChildren()) {
                        DataCart dataCart = item.getValue(DataCart.class);
                        getCategori(dataCart.getIdMenu());
                        dataCartList.add(dataCart);
                        total = total + Integer.parseInt(dataCart.getHargaMenu());
                    }
                    totalHarga.setText(String.valueOf(total));
                    databaseReference.removeEventListener(listener);
                } else {
                    Log.i("data kosong", "data cart tidak ditemukan");
                    totalHarga.setText("0");
                    dataMenuList.clear();
                    dataCartList.clear();
                    callRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getCategori(String idMenu) {
        listener = databaseReference.child("data-barang").orderByChild("id").equalTo(idMenu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        DataMenu dataMenu = item.getValue(DataMenu.class);
                        dataMenuList.add(dataMenu);
                    }
                    databaseReference.removeEventListener(listener);

                    if (dataMenuList.size() == dataCartList.size()) {
                        callRecycler();
                    }
                } else {
                    Log.i("data kosong", "data menu tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void callRecycler() {
        recyclerView.getRecycledViewPool().clear();
        adapter = new CheckoutAdapter(getContext(), dataCartList, dataMenuList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

//    function to checkout menu

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkoutMenu() {
        String idTransaksi = String.valueOf(System.currentTimeMillis());
        String userLocation = location.getText().toString();
        String deliverStatus = "Shipping";
        String paymentStatus = "Verified";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = String.valueOf(formatter.format(now));
        String totalPrice = totalHarga.getText().toString();

        HashMap<String, Object> idCart = new HashMap<>();
        idCart.clear();


        databaseReference.child("cart").orderByChild("idCustomer").equalTo(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                HashMap<String, Object> cart = new HashMap<>();
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        DataCart dataCart = item.getValue(DataCart.class);
                        cart.put("idCart" + i, dataCart.getIdCart());
                        i++;
                    }
                    idCart.put("idCart", cart);
                    DataHistory transaction = new DataHistory(userLocation, deliverStatus, cart, userUID, idTransaksi, paymentStatus, dateTime, totalPrice);
                    dataTransaction(transaction);
                } else {
                    Log.i("Error ", "Data Cart Not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void dataTransaction(DataHistory dataHistory) {
        databaseReference.child("transaction").child(dataHistory.getIdTransaksi()).setValue(dataHistory).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                moveCartToHistory(dataHistory);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Barang Gagal Ditambah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveCartToHistory(DataHistory dataHistory) {
        Object[] idCartValue = dataHistory.getIdCart().values().toArray();
        for (int i = 0; i < idCartValue.length; i++) {
            databaseReference.child("cart").orderByChild("idCart").equalTo(idCartValue[i].toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            DataCart dataCart = item.getValue(DataCart.class);
                            databaseReference.child("history").child(dataCart.getIdCart()).setValue(dataCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    removeCart(dataCart.getIdCart());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Barang Gagal Dipindahkan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Log.i("Error In Move Cart", "Snapshot not found");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void removeCart(String idCart) {
        databaseReference.child("cart").child(idCart).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showAllCart();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Barang Gagal Dihapus Dari Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    function to checkout menu

    @Override
    public void onResume() {
        super.onResume();
        total = 0;
        showAllCart();
    }
}