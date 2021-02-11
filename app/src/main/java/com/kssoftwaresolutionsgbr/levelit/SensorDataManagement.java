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
    private Bluetooth bluetooth;
    private DataProcessing dataProcessing;


    private Float Angle;
    private ChangeListener listener;

    public boolean useExternalSensor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        accelerometer = new Accelerometer(this);
        bluetooth = new Bluetooth();
        dataProcessing = new DataProcessing();

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!useExternalSensor){
                    try{
                        Angle = dataProcessing.getAngle(tx, ty);
                    } catch (Exception e){}
                }
            }
        });

        bluetooth.setListener(new Bluetooth.ChangeListener() {
            @Override
            public void onChange() {
                if(useExternalSensor){
                    try{
                        Angle = dataProcessing.getAngle(bluetooth.rxData);
                    } catch (DataProcessingException e){
                    }
                }
            }
        });

    }

    public Float getAngle(){
         return Angle;
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }

    public void stop_sensor(){
        accelerometer.unregister();
    }

    public void start_sensor(){
        if(useExternalSensor){
            try {
                if(!bluetooth.isConnected()){
                    bluetooth.openConnection();
                }
            } catch (BluetoothException e) {
                e.printStackTrace();
            }
        }
        else{
            accelerometer.register();
        }
    }

}
