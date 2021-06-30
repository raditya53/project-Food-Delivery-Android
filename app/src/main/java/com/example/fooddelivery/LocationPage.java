package com.example.fooddelivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;
import java.util.Locale;


public class LocationPage extends Activity implements LocationListener{
    private LocationManager locationManager;
    private final int LOCATION_REQUEST_CODE = 1001;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SHARED_LOCATION = "location";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_page);
        if(ContextCompat.checkSelfPermission(LocationPage.this, Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED) {
           askLocationPermission();
        } else {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, LocationPage.this);
        } catch (Exception e) {
            Toast.makeText(LocationPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(LocationPage.this, ""+location.getLatitude()+" " +location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(LocationPage.this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            String address = addressList.get(0).getAddressLine(0);
            Intent intent = new Intent(LocationPage.this, NavigasiTab.class);
//            intent.putExtra("address", address);
            saveLocation(address);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(LocationPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(String address) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SHARED_LOCATION, address);
        editor.apply();
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(LocationPage.this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
                askLocationPermission();
            }
        }
    }
}

