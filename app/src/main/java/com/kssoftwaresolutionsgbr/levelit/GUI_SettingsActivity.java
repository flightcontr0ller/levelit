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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class GUI_SettingsActivity extends AppCompatActivity {

    // fields
    protected BO_APP_SensorDataManagement SDM;
    private Button bt_nav_main;
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
        if(sw_warning.isChecked()){
            if (valid_input(Float.parseFloat(et_warning_angle.getText().toString()))){
                SDM.warningAngle = warning_angle;
                Intent intent = new Intent(this, GUI_MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),  "invalid warning angle", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Intent intent = new Intent(this, GUI_MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean valid_input(Float raw_input){
        try {
            warning_angle = (int)Math.round(Float.valueOf(raw_input));
        } catch (Exception exception){
            return false;
        }
        if(warning_angle > 45 || warning_angle <= 0){
            return false;
        }
        else{
            return true;
        }

    }
}