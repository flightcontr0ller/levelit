package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // fields
    private Accelerometer accelerometer;
    private CalculatorLocal calculatorLocal;
    private Bluetooth bluetooth;
    private TextView tv_alignment;
    private TextView tv_rxdata;
    private TextView tv_btdebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new Accelerometer(this);
        calculatorLocal = new CalculatorLocal();
        bluetooth = new Bluetooth();

        tv_alignment = (TextView)findViewById(R.id.tv_alignment);
        tv_rxdata = (TextView)findViewById(R.id.tv_rxdata);
        tv_btdebug = (TextView)findViewById(R.id.tv_btdebug);

        bluetooth.findBT();
        try {
            bluetooth.openBT();
        } catch (Exception e){}



        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                calculatorLocal.get_data(tx, ty, tz);

                tv_rxdata.setText(bluetooth.rxData);
                tv_btdebug.setText(bluetooth.debugMsg);

                // output local alignment
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

        try {
            bluetooth.closeBT();
        } catch (Exception e){}
    }
}