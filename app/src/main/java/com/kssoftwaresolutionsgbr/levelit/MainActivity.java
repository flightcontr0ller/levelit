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

        // create button and textview
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



        /*
        bluetooth.findBT();
        try {
            bluetooth.openBT();
        } catch (Exception e){}
*/

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                calculatorLocal.get_data(tx, ty, tz);
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
        } catch (Exception e){}
    }
}