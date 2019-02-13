package com.products.ammar.sem_cut;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ChooserActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
    }

    public void driverClick(View view) {
        Intent intent = new Intent(this, DriverActivity.class);
        startActivity(intent);
    }

    public void paddocksClick(View view) {
        Intent intent = new Intent(this, PaddocksMainActivity.class);
        startActivity(intent);
    }
}
