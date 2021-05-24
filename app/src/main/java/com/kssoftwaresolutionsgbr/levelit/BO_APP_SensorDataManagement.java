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
        void onChange(Integer Angle);
    }
    public interface DisplayIsHorizontalListener{
        void onChange(Boolean DisplayIsHorizontal);
    }
    public interface IsConnectedListener{
        void onChange(boolean state);
    }

    // fields
    private BO_MOD_Accelerometer Accelerometer;
    private BO_MOD_Bluetooth Bluetooth;
    private BO_MOD_DataProcessing DataProcessing;

    private Integer currentAngle = 0;
    private AngleListener angleListener;

    private Boolean DisplayIsHorizontal = false;
    private DisplayIsHorizontalListener displayIsHorizontalListener;

    private boolean IsConnected;
    private IsConnectedListener isConnectedListener;

    public boolean useExternalSensor = false;

    public boolean warningsActive = false;
    public Integer warningAngle = 0;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        Accelerometer = new BO_MOD_Accelerometer(this);
        Bluetooth = new BO_MOD_Bluetooth();
        DataProcessing = new BO_MOD_DataProcessing();

        this.angleListener = null;
        this.isConnectedListener = null;

        Accelerometer.setAccelerometerListener(new BO_MOD_Accelerometer.AccelerometerListener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(!useExternalSensor){
                    try{
                        DisplayIsHorizontal = DataProcessing.getIsHorizontal();
                        currentAngle = DataProcessing.getAngle(tx, ty);
                        if (angleListener != null && displayIsHorizontalListener != null){
                            angleListener.onChange(currentAngle);
                            displayIsHorizontalListener.onChange(DisplayIsHorizontal);
                        }

                    } catch (BO_MOD_DataProcessingException e){
                        Log.e("DataProcessing", e.getMessage());
                    }
                }

            }
        });

        Bluetooth.setBluetoothDataListener(new BO_MOD_Bluetooth.BluetoothDataListener() {
            @Override
            public void onChange(String receivedData) {
                if(useExternalSensor){
                    try{
                        currentAngle = DataProcessing.getAngle(receivedData);
                        if (angleListener != null){
                            angleListener.onChange(-currentAngle); // the value gets inverted that the gui can display it correctly
                        }
                    } catch (BO_MOD_DataProcessingException e){
                        Log.e("DataProcessing", e.getMessage());
                    }
                }
            }
        });

        Bluetooth.setBluetoothIsConnectedListener(new BO_MOD_Bluetooth.BluetoothIsConnectedListener() {
            @Override
            public void onChange(boolean state) {
                IsConnected = state;
                if (isConnectedListener != null){
                    isConnectedListener.onChange(IsConnected);
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

    public boolean IsBluetoothConnected(){
        return IsConnected;
    }

    public void setAngleListener(AngleListener listener) {
        this.angleListener = listener;
    }

    public void setDisplayIsHorizontalListener(DisplayIsHorizontalListener listener){
        this.displayIsHorizontalListener = listener;
    }

    public void setIsConnectedListener(IsConnectedListener listener){
        this.isConnectedListener = listener;
    }

}
