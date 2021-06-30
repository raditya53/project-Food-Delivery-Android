package com.example.fooddelivery;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateAccount extends AppCompatActivity {
ImageButton imageButton;
EditText email, password, fullname,phonenumber;
ImageView profilePict;
Button btnUpdate;

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String url,userUID;
    private Uri uri;
    private boolean imgIsChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullname = findViewById(R.id.nama);
        phonenumber = findViewById(R.id.nomorhp);
        profilePict = findViewById(R.id.photo);
        btnUpdate = findViewById(R.id.button_update);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = firebaseUser.getUid();

        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));
        password.setText(intent.getStringExtra("password"));
        fullname.setText(intent.getStringExtra("nama"));
        phonenumber.setText(intent.getStringExtra("phone"));
        url = intent.getStringExtra("url");
        imgIsChanged = false;
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();


        Picasso.with(UpdateAccount.this).load(url).into(profilePict);

        profilePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

        imageButton = findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateAccount.this, NavigasiTab.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getExtensionImage(Uri uri) {
        ContentResolver Cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            imgIsChanged = true;
        }
        Picasso.with(this).load(uri).into(profilePict);
    }

    private void updateInfo() {
        if (fullname.getText().toString().isEmpty()) {
            fullname.setError("Email Field Cannot Be Empty");
            fullname.requestFocus();
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Password Field Cannot Be Empty");
            password.requestFocus();
        }

        if (password.getText().toString().length() <= 6) {
            password.setError("Password Length Must Be Greater Than 6");
            password.requestFocus();
        }

        //if img changes
        if (imgIsChanged == true) {
            if (!url.equals("https://firebasestorage.googleapis.com/v0/b/project-android-19de2.appspot.com/o/userPicture%2Fdefault_profile_picture.png?alt=media&token=f9bd96de-f45b-4cef-a3b3-ed82c3c160ce")) {
                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("userPicture").child(System.currentTimeMillis() + "." + getExtensionImage(uri));
                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(UpdateAccount.this, "Image Updated", Toast.LENGTH_SHORT).show();
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());
                                Uri downloadUri = uriTask.getResult();
                                uploadData(downloadUri.toString());
                            }
                        });
                    }
                });
            } else {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("userPicture").child(System.currentTimeMillis() + "." + getExtensionImage(uri));
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UpdateAccount.this, "Image Updated", Toast.LENGTH_SHORT).show();
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        uploadData(downloadUri.toString());
                    }
                });

            }
        } else {
            uploadData(url);
        }
        }

    private void uploadData(String imgUri) {
        DataUser dataUser = new DataUser(userUID, fullname.getText().toString(), email.getText().toString(), password.getText().toString(), phonenumber.getText().toString(), imgUri);
        databaseReference.child(userUID).setValue(dataUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateAccount.this, "Update Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
