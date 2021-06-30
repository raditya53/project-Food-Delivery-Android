package com.example.fooddelivery;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddMenu extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private EditText etNama, etDeskripsi, etHarga, etKategori;
    private TextView tvShowAll;

    private Uri ImageUri;
    private int isComplete = 0;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nambahfoto);
        btnChoose = findViewById(R.id.btn_choose);
        btnUpload = findViewById(R.id.uploadbtn);
        imageView = findViewById(R.id.imgContainer);
        etNama = findViewById(R.id.et_nama);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        tvShowAll = findViewById(R.id.showHome);
        etKategori = findViewById(R.id.et_kategori);

        databaseReference = FirebaseDatabase.getInstance().getReference("data-barang");
        storageReference = FirebaseStorage.getInstance().getReference("pictures");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isComplete == 1) {
                    Toast.makeText(AddMenu.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver Cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();

            Picasso.with(this).load(ImageUri).into(imageView);
        }
    }

    private void uploadFile() {
        if(ImageUri != null) {
            isComplete = 1;

            //send img to storage firebase
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));
            storageTask = fileReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                        Uri downloadedUri = uriTask.getResult();

                        String id = String.valueOf(System.currentTimeMillis());

                        DataMenu dataMenu = new DataMenu(id, etNama.getText().toString(),etDeskripsi.getText().toString(), etHarga.getText().toString(),downloadedUri.toString(),etKategori.getText().toString());
                        databaseReference.child(id).setValue(dataMenu);

                        if(storageTask.isSuccessful()) {
                            etNama.setText("");
                            etDeskripsi.setText("");
                            etHarga.setText("");
                            etKategori.setText("");
                            imageView.setImageResource(0);
                            isComplete = 0;
                        }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
