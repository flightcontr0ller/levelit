/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        SettingsActivity
 * dev:          Malte Schoenert
 * created on:   2021-02-09
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class GUI_SettingsActivity extends AppCompatActivity {

    // fields
    protected BO_APP_SensorDataManagement SDM;
    private Button bt_nav_main;
    private Switch sw_extsensor;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Assign backend
        SDM = (BO_APP_SensorDataManagement)getApplication();

        bt_nav_main = findViewById(R.id.bt_nav_main);
        bt_nav_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_main_activity();
            }
        });

        sw_extsensor = findViewById(R.id.sw_extsensor);
        sw_extsensor.setChecked(SDM.useExternalSensor);
        sw_extsensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SDM.useExternalSensor = sw_extsensor.isChecked();
            }
        });


    }

    private void open_main_activity(){
        Intent intent = new Intent(this, GUI_MainActivity.class);
        startActivity(intent);
    }
}