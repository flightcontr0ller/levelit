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
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GUI_MainActivity extends AppCompatActivity {

    // fields
    private Button bt_nav_settings;
    private TextView tv_angle;
    private TextView tv_sensor;
    private TextView tv_connection;
    protected BO_APP_SensorDataManagement SDM;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign backend
        SDM = (BO_APP_SensorDataManagement)getApplication();

        // create button and textView
        tv_angle = findViewById(R.id.tv_angle);
        bt_nav_settings = findViewById(R.id.bt_nav_settings);
        bt_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings_activity();
            }
        });

        // create TextViews in Toolbar
        tv_sensor = findViewById(R.id.tv_sensor);
        if (SDM.useExternalSensor){
            tv_sensor.setText("external");
        }
        else{
            tv_sensor.setText("local");
        }

        tv_connection = findViewById(R.id.tv_connection);
        if(SDM.IsBluetoothConnected()){
            tv_connection.setText("connected");
        }
        else{
            tv_connection.setText("not connected");
        }



        SDM.setAngleListener(new BO_APP_SensorDataManagement.AngleListener() {
            @Override
            public void onChange(Integer Angle) {
                tv_angle.setText(Integer.toString(Angle));
            }
        });

        SDM.setIsConnectedListener(new BO_APP_SensorDataManagement.IsConnectedListener() {
            @Override
            public void onChange(boolean BluetoothIsConnected) {
                if(BluetoothIsConnected){
                    tv_connection.setText("connected");
                }
                else{
                    tv_connection.setText("not connected");
                }
            }
        });

    }

    private void open_settings_activity(){
        Intent intent = new Intent(this, GUI_SettingsActivity.class);
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