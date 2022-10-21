package com.example.lab04;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    TextView tvToaDo,tvLink;
    Button btnBai2;
    private FusedLocationProviderClient fusedLocationClient;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            Log.d("TAG", "Đã cấp quyền vị trí chính xác tuyệt đối");

                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                            Log.d("TAG", "Đã cấp quyền vị trí chính xác tương đối");

                        } else {
                            // No location access granted.
                            Log.d("TAG", "Bị từ chối cấp quyền vị trí");
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnBai2 = findViewById(R.id.btnBai2);
        btnBai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            String toaDo = location.getLatitude() + " - " + location.getLongitude();
                            tvToaDo = findViewById(R.id.tvToaDo);
                            tvLink = findViewById(R.id.tvLink);
                            tvToaDo.setText(toaDo);
                            Log.d("TAG", "onSuccess: Tọa độ: lat = " + location.getLatitude() );
                            Log.d("TAG", "onSuccess: lng = " + location.getLongitude() );

                            // tạo chuỗi hiển thị trên web
                            String msg1 = "http://www.google.com/maps/search/?api=1&query=" + location.getLatitude() + "%2C" + location.getLongitude();
                            tvLink.setText(msg1);
                            Log.d("TAG", "Xem trên bản đồ " + msg1);

                        }else{
                            Log.d("TAG", "onSuccess: Location null ");
                        }
                    }
                });
    }
}