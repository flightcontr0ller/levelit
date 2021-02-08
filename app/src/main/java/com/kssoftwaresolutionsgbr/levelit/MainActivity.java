package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // fields
    private Accelerometer accelerometer;
    private CalculatorLocal calculatorLocal;
    private TextView tv_alignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_alignment = (TextView)findViewById(R.id.tv_alignment);
        accelerometer = new Accelerometer(this);
        calculatorLocal = new CalculatorLocal();

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                calculatorLocal.get_data(tx, ty, tz);

                if(calculatorLocal.alignment == Alignment.UPWARD){
                    tv_alignment.setText("upward");
                }
                else if (calculatorLocal.alignment == Alignment.DOWNWARD){
                    tv_alignment.setText("downward");
                }
                else if (calculatorLocal.alignment == Alignment.RIGHTWARD){
                    tv_alignment.setText("rightward");
                }
                else if (calculatorLocal.alignment == Alignment.LEFTWARD){
                    tv_alignment.setText("leftward");
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