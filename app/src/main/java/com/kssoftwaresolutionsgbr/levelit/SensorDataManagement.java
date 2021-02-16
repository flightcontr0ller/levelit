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

    public interface AngleListener{
        void onChange(Float Angle);
    }

    // fields
    private Accelerometer accelerometer;
    private Bluetooth bluetooth;
    private DataProcessing dataProcessing;

    private Float currentAngle = Float.valueOf(0);
    private AngleListener listener;

    public boolean useExternalSensor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        accelerometer = new Accelerometer(this);
        bluetooth = new Bluetooth();
        dataProcessing = new DataProcessing();

        this.listener = null;

        accelerometer.setAccelerometerListener(new Accelerometer.AccelerometerListener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!useExternalSensor){
                    try{
                        currentAngle = dataProcessing.getAngle(tx, ty);
                        if (listener != null){
                            listener.onChange(currentAngle);
                        }
                    } catch (DataProcessingException e){}
                }

            }
        });

        bluetooth.setBluetoothListener(new Bluetooth.BluetoothListener() {
            @Override
            public void onChange(String receivedData) {
                if(useExternalSensor){
                    try{
                        currentAngle = dataProcessing.getAngle(receivedData);
                        if (listener != null){
                            listener.onChange(currentAngle);
                        }
                    } catch (DataProcessingException e){}
                }
            }
        });

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

    public void setAngleListener(AngleListener listener) {
        this.listener = listener;
    }

}
