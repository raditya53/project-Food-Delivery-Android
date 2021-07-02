package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AccountFragment extends Fragment {
private Button update, logout,aboutUs, history;

private DatabaseReference databaseReference;
private FirebaseAuth firebaseAuth;

private String userUID;

private ImageView photo;
private TextView nama;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update = view.findViewById(R.id.button_update);
        history = view.findViewById(R.id.button_history);
        aboutUs = view.findViewById(R.id.aboutUs);
        logout = view.findViewById(R.id.button_logout);
        photo = view.findViewById(R.id.photo);
        nama = view.findViewById(R.id.accountName);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        userUID = firebaseAuth.getUid();

        getUserData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("idUser").equalTo(userUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()) {
                            DataUser dataUser = data.getValue(DataUser.class);
                            Intent intent = new Intent(getContext(), UpdateAccount.class);
                            intent.putExtra("nama", dataUser.getFullname());
                            intent.putExtra("email", dataUser.getEmail());
                            intent.putExtra("password", dataUser.getPassword());
                            intent.putExtra("phone", dataUser.getPhoneNumber());
                            intent.putExtra("url", dataUser.getProfileImage());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_History.class);
                startActivity(intent);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Under Development, Thanks!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), About_us.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), MultiPage.class);
                startActivity(intent);
            }
        });
    }

    private void getUserData() {
        databaseReference.orderByChild("idUser").equalTo(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()) {
                    DataUser dataUser = item.getValue(DataUser.class);
                    nama.setText(dataUser.getFullname());
                    Picasso.with(getContext()).load(dataUser.getProfileImage()).into(photo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}