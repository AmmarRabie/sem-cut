package com.products.ammar.sem_cut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BlutoothActivity extends AppCompatActivity implements IBlutoothHelper.OnReceiveDataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blutooth);
        BlutoothHelper bHelper = new BlutoothHelper(this);
        bHelper.setOnReceiveData(this);

        bHelper.send("Hi, this is a test message".getBytes());
    }


    @Override
    public void receive(byte[] data) {
        Toast.makeText(this, new String(data), Toast.LENGTH_SHORT).show();
    }
}