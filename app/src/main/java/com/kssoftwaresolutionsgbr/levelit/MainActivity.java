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
    private Accelerometer accelerometer;
    private CalculatorLocal calculatorLocal;
    private Bluetooth bluetooth;
    private CalculatorExternal calculatorExternal;
    private boolean use_external_sensor;

    private Button bt_nav_settings;
    private TextView tv_angle;


    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // catch intent from settings
        use_external_sensor = getIntent().getBooleanExtra("use_external_sensor",false);

        // create button and textView
        bt_nav_settings = findViewById(R.id.bt_nav_settings);
        bt_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings_activity();
            }
        });
        tv_angle = findViewById(R.id.tv_angle);

        // create objects
        accelerometer = new Accelerometer(this);
        calculatorLocal = new CalculatorLocal();
        bluetooth = new Bluetooth();
        calculatorExternal = new CalculatorExternal();

        // start Bluetooth if needed
        if(use_external_sensor){
            try {
                bluetooth.findBT();
                bluetooth.openBT();
            } catch (BluetoothException e){
                Snackbar.make(findViewById(R.id.main_activity), e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }

        // listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!use_external_sensor){
                    try{
                        tv_angle.setText(calculatorLocal.getAngle(tx, ty));
                    } catch (Exception e){
                        tv_angle.setText("");
                        Snackbar.make(findViewById(R.id.main_activity), R.string.warning_local_sensor, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bluetooth.setListener(new Bluetooth.ChangeListener() {
            @Override
            public void onChange() {
                if(use_external_sensor){
                    try{
                        tv_angle.setText(calculatorExternal.getAngle(bluetooth.getRxData()));
                    } catch (Exception e){
                        tv_angle.setText("");
                        Snackbar.make(findViewById(R.id.main_activity), R.string.warning_external_sensor, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void open_settings_activity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("use_external_sensor", use_external_sensor);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometer.unregister();
        try {
            bluetooth.closeBT();
        } catch (BluetoothException e){
            Snackbar.make(findViewById(R.id.main_activity), e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

}