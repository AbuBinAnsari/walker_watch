package com.example.walker_watch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView textView, textViewLat, textViewLong, textViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textViewId);
        textViewLat = (TextView) findViewById(R.id.textViewLatId);
        textViewLong = (TextView) findViewById(R.id.textViewLongId);
        textViewAddress = (TextView) findViewById(R.id.textViewAddressId);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }
        public void startListening(){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }



        public void updateLocationInfo(Location location){
            TextView textViewLatId = findViewById(R.id.textViewLatId);
            TextView textViewLongId = findViewById(R.id.textViewLongId);
            TextView textViewAddressId = findViewById(R.id.textViewAddressId);

            textViewLatId.setText("Latitude: " + Double.toString(location.getLatitude()));
            textViewLongId.setText("Longitude: " + Double.toString(location.getLongitude()));

            String address = "Could not find address :(";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddress != null && listAddress.size() > 0){
                address = "Address:\n";

                if (listAddress.get(0).getThoroughfare() != null){
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }
                if (listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality() + " ";
                }
                if (listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode() + " ";
                }
                if (listAddress.get(0).getAdminArea() != null){
                    address += listAddress.get(0).getAdminArea();
                }

            }

            }catch (Exception e){
                e.printStackTrace();
            }
            textViewAddressId.setText(address);
        }
    }

