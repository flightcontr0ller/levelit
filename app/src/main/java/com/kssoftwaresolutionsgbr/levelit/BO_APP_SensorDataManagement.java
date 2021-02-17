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
import android.util.Log;

public class BO_APP_SensorDataManagement extends Application {
/*
This Class is the backend of this app. Bluetooth data and local sensor readings get controlled from here.
 */

    public interface AngleListener{
        void onChange(Float Angle);
    }

    // fields
    private BO_MOD_Accelerometer Accelerometer;
    private BO_MOD_Bluetooth Bluetooth;
    private BO_MOD_DataProcessing DataProcessing;

    private Float currentAngle = Float.valueOf(0);
    private AngleListener listener;

    public boolean useExternalSensor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        Accelerometer = new BO_MOD_Accelerometer(this);
        Bluetooth = new BO_MOD_Bluetooth();
        DataProcessing = new BO_MOD_DataProcessing();

        this.listener = null;

        Accelerometer.setAccelerometerListener(new BO_MOD_Accelerometer.AccelerometerListener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!useExternalSensor){
                    try{
                        currentAngle = DataProcessing.getAngle(tx, ty);
                        if (listener != null){
                            listener.onChange(currentAngle);
                        }
                    } catch (BO_MOD_DataProcessingException e){
                        Log.e("DataProcessing", e.getMessage());
                    }
                }

            }
        });

        Bluetooth.setBluetoothListener(new BO_MOD_Bluetooth.BluetoothListener() {
            @Override
            public void onChange(String receivedData) {
                if(useExternalSensor){
                    try{
                        currentAngle = DataProcessing.getAngle(receivedData);
                        if (listener != null){
                            listener.onChange(currentAngle);
                        }
                    } catch (BO_MOD_DataProcessingException e){
                        Log.e("DataProcessing", e.getMessage());
                    }
                }
            }
        });

    }

    public void stop_sensor(){
        Accelerometer.unregister();
    }

    public void start_sensor(){
        if(useExternalSensor){
            try {
                if(!Bluetooth.isConnected()){
                    Bluetooth.openConnection();
                }
            } catch (BO_MOD_BluetoothException e) {
                Log.e("Bluetooth", e.getMessage());
            }
        }
        else{
            Accelerometer.register();
        }
    }

    public void setAngleListener(AngleListener listener) {
        this.listener = listener;
    }

}
