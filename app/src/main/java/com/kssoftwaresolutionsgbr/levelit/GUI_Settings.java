/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        GUI_SettingsActivity
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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class GUI_Settings extends AppCompatActivity {
/*
This activity includes UI elements to set up the app behavior. It is possible to switch between
sensors and set a warning at a special angle.
 */

    // fields
    protected BO_APP_SensorDataManagement SDM;
    private Button bt_nav_main;
    private Button bt_nav_about;
    private Switch sw_extsensor;
    private Switch sw_warning;
    private EditText et_warning_angle;

    private Integer warning_angle;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Assign backend
        SDM = (BO_APP_SensorDataManagement)getApplication();

        bt_nav_main = findViewById(R.id.bt_nav_settings_2);
        bt_nav_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_main_activity();
            }
        });

        bt_nav_about = findViewById(R.id.bt_nav_about);
        bt_nav_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_about_activity();
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


        et_warning_angle = findViewById(R.id.et_warning_angle);
        et_warning_angle.setText(String.valueOf(SDM.warningAngle));
        et_warning_angle.setEnabled(SDM.warningsActive);

        sw_warning = findViewById(R.id.sw_warning);
        sw_warning.setChecked(SDM.warningsActive);
        sw_warning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SDM.warningsActive = sw_warning.isChecked();
                et_warning_angle.setEnabled(sw_warning.isChecked());
            }
        });
    }

    private void open_main_activity(){
        if(valid_input()){
            lock_ui();
            start_main_activity();
        }
        else{
            Toast.makeText(getApplicationContext(),  "invalid warning angle", Toast.LENGTH_SHORT).show();
        }
    }

    private void lock_ui(){
        bt_nav_main.setEnabled(false);
        bt_nav_about.setEnabled(false);
        sw_extsensor.setEnabled(false);
        sw_warning.setEnabled(false);
        et_warning_angle.setEnabled(false);
    }

    private void start_main_activity(){
        Intent intent = new Intent(this, GUI_Main.class);
        startActivity(intent);
    }

    private boolean valid_input(){
        if(sw_warning.isChecked()){
            try {
                warning_angle = Math.round(Float.valueOf(Float.parseFloat(et_warning_angle.getText().toString())));
            } catch (Exception exception){
                return false;
            }
            if(warning_angle > 45 || warning_angle <= 0){
                return false;
            }
            else{
                SDM.warningAngle = warning_angle;
                return true;
            }
        }
        else{
            return true;
        }
    }

    private void open_about_activity(){
        lock_ui();
        Intent intent = new Intent(this, GUI_About.class);
        startActivity(intent);
    }
}