package com.products.ammar.sem_cut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class PaddocksMainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Button changeStatusView;
    private DatabaseReference root;
    private boolean mIsRunning;

    private FirebaseRealTime db;
    private BlutoothHelper bHelper;
    private LatLng currLocation;

    private GoogleMap mMap;
    private Marker driverMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddocks);
        changeStatusView = findViewById(R.id.changeStatus);
        changeStatusView.setOnClickListener(this);
        root = FirebaseDatabase.getInstance().getReference();


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
                currLocation = new LatLng(location.latitude, location.longitude);
                driverMarker.setPosition(currLocation);
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

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.paddocksActivity_map)).getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        driverMarker = mMap.addMarker(new MarkerOptions().position(Constants.TRACK_START_LOCATION).title("driver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.TRACK_START_LOCATION));
    }
}