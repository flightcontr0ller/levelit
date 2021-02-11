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

public class SensorDataManagement extends Application {
/*
This Class is the backend of this app. Bluetooth data and local sensor readings get controlled from here.
 */

    // fields
    private Accelerometer accelerometer;
    private Float local_Angle;

    private Bluetooth bluetooth;
    private DataProcessing dataProcessing;
    private Float external_Angle;

    public Float Angle;
    public boolean use_external_sensor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        accelerometer = new Accelerometer(this);
        bluetooth = new Bluetooth();
        dataProcessing = new DataProcessing();

        use_external_sensor = true;

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!use_external_sensor){
                    try{
                        local_Angle = dataProcessing.getAngle(tx, ty);
                    } catch (Exception e){}
                }
            }
        });

        bluetooth.setListener(new Bluetooth.ChangeListener() {
            @Override
            public void onChange() {
                if(use_external_sensor){
                    try{
                        external_Angle = dataProcessing.getAngle(bluetooth.rxData);
                    } catch (DataProcessingException e){
                    }
                }
            }
        });

    }

    public String getAngle(){
        if(use_external_sensor){
            return Float.toString(external_Angle);
        }
        else{
            return Float.toString(local_Angle);
        }
    }

    public void stop_sensor(){
        accelerometer.unregister();
    }

    public void start_sensor(){
        if(use_external_sensor){
            try {
                bluetooth.openConnection();
            } catch (BluetoothException e) {
                e.printStackTrace();
            }
        }
        else{
            accelerometer.register();
        }
    }

}
