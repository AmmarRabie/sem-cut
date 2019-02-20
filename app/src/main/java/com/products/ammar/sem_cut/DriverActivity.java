package com.products.ammar.sem_cut;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.products.ammar.sem_cut.App.Constants;

import java.io.UnsupportedEncodingException;

public class DriverActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Button changeStatusView;
    private DatabaseReference root;
    private boolean mIsRunning;
    private boolean mIsMapVisible;

    private BlutoothHelper bHelper;
    private LatLng currLocation;
    private SupportMapFragment mapView;
    private GoogleMap mMap;
    private Marker driverMarker;

    private FirebaseRealTime db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        changeStatusView = findViewById(R.id.changeStatus);
        changeStatusView.setOnClickListener(this);
        root = FirebaseDatabase.getInstance().getReference();
        mIsMapVisible = true;

        handleDbChange();

        bHelper = new BlutoothHelper(this);
        bHelper.setOnReceiveData(new IBlutoothHelper.OnReceiveDataListener() {
            @Override
            public void receive(byte[] data) {
                String str = null; // for UTF-8 encoding
                try {
                    str = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("BLUE", str);
//                Toast.makeText(DriverActivity.this, str, Toast.LENGTH_SHORT).show();
//                int rpm;
//                int speed;
//                boolean isRunning;
//                db.setRpm(rpm);
//                db.setSpeed(speed);
//                db.setStatus(isRunning);
            }
        });
        bHelper.start();

        bHelper.send("5".getBytes());
        handleLocationChange();


        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.driverActivity_map);
        mapView.getMapAsync(this);
    }

    private void handleDbChange() {
        db = new FirebaseRealTime(new IAppRealTime.OnDataChange() {
            @Override
            public void onRpmChange(int newValue) {
                TextView currView = findViewById(R.id.rpm);
                updateOneView(currView, newValue);
            }

            @Override
            public void onSpeedChange(int newValue) {
                TextView currView = findViewById(R.id.speed);
                updateOneView(currView, newValue);
            }

            @Override
            public void onLocationChange(LatLng location) {
//                Toast.makeText(DriverActivity.this, "Location change db", Toast.LENGTH_SHORT).show();
//                currLocation = new LatLng(location.latitude, location.longitude);
//                driverMarker.setPosition(currLocation);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
            }

            @Override
            public void onStatusChange(boolean isRunning) {
                if (isRunning) {
                    changeStatusView.setText("Close");
                    mIsRunning = true;
                } else {
                    changeStatusView.setText("Open");
                    mIsRunning = false;
                }
            }
        });
    }

    private void handleLocationChange() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
//                Toast.makeText(DriverActivity.this, "listen for new location", Toast.LENGTH_SHORT).show();
                // Called when a new location is found by the network location provider.
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currLocation = new LatLng(latitude, longitude);
                db.setLocation(currLocation);
//                Toast.makeText(DriverActivity.this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();
                // update instead when db change
                driverMarker.setPosition(currLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText(DriverActivity.this, "location provider enabled", Toast.LENGTH_SHORT).show();
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            return;
        }
        if (locationManager == null) {
            Toast.makeText(this, "location manger not found", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private <T> void updateOneView(TextView view, T data) {
        view.setText(data.toString());
    }

    @Override
    public void onClick(View view) {
        if (mIsRunning) {
            root.child("status").setValue(false);
        } else {
            root.child("status").setValue(true);
        }
    }

    public void onShowHideMapClick(View view) {
        if (mIsMapVisible) {
            ((Button) view).setText("hide");
            mapView.getView().setVisibility(View.VISIBLE);
        } else {
            ((Button) view).setText("show");
            mapView.getView().setVisibility(View.GONE);
        }
        mIsMapVisible = !mIsMapVisible;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        driverMarker = mMap.addMarker(new MarkerOptions().position(Constants.TRACK_START_LOCATION).title("driver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.TRACK_START_LOCATION));
    }
}
