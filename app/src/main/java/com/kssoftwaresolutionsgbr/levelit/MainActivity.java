/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        MainActivity
 * dev:          Malte Schoenert
 * created on:   2021-02-08
 ************************************************
 */
package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // fields
    private Button bt_update;
    private Button bt_nav_settings;
    private TextView tv_angle;
    protected SensorDataManagement SDM;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign backend
        SDM = (SensorDataManagement)getApplication();

        // create button and textView
        tv_angle = findViewById(R.id.tv_angle);
        bt_nav_settings = findViewById(R.id.bt_nav_settings);
        bt_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings_activity();
            }
        });

        bt_update = findViewById(R.id.bt_update);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_angle.setText(Float.toString(SDM.getAngle()));
            }
        });


        SDM.setListener(new SensorDataManagement.ChangeListener() {
            @Override
            public void onChange() {
                tv_angle.setText(Float.toString(SDM.getAngle()));
            }
        });

    }

    private void open_settings_activity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDM.start_sensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SDM.stop_sensor();
    }

}