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

public class SettingsActivity extends AppCompatActivity {

    // fields
    private Bluetooth bluetooth;
    private Button bt_nav_main;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bt_nav_main = findViewById(R.id.bt_nav_main);
        bt_nav_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_main_activity();
            }
        });

    }

    private void open_main_activity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}