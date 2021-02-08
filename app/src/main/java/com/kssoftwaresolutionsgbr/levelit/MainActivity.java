package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // fields
    private Accelerometer accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new Accelerometer(this);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                // testing of acc sensor
                if(tx > 1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
                else if(tx < -1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
            }
        });
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
    }
}