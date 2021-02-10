/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        SensorDataManagement
 * dev:          Malte Schoenert
 * created on:   2021-02-10
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

import android.app.Application;

import com.google.android.material.snackbar.Snackbar;

public class SensorDataManagement extends Application {
/*
This Class is the backend of this app. Bluetooth data and local sensor readings get controlled from here.

 */

    // fields
    private Accelerometer accelerometer;
    private CalculatorLocal calculatorLocal;
    private Bluetooth bluetooth;
    private CalculatorExternal calculatorExternal;
    private Float local_X, local_Y, local_Z;
    private Float local_Angle;
    private Float external_Angle;

    public boolean use_external_sensor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        accelerometer = new Accelerometer(this);
        calculatorLocal = new CalculatorLocal();
        bluetooth = new Bluetooth();
        calculatorExternal = new CalculatorExternal();

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!use_external_sensor){
                    try{
                        local_Angle = calculatorLocal.getAngle(tx, ty);
                    } catch (Exception e){}
                }
            }
        });

        accelerometer.register();

        bluetooth.setListener(new Bluetooth.ChangeListener() {
            @Override
            public void onChange() {
                if(use_external_sensor){
                    try{
                        external_Angle = calculatorExternal.getAngle(bluetooth.getRxData());
                    } catch (Exception e){
                    }
                }
            }
        });

    }


}
