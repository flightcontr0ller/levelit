/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        Bluetooth
 * dev:          Malte Schoenert
 * created on:   2021-02-08
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth {
    /*
    This class includes all methods to establish a bluetooth connection to the external sensor and sending as well as receiving data.
    The bluetooth SSID of the external sensor is set in the variable BluetoothDevice.
    To start receiving data run: findDevice(), connectDevice()
    To stop receiving data run: closeConnection()
     */

    // interfaces as observer
    public interface BluetoothListener {
        void onChange(String receivedData);
    }

    // fields
    public static final String BluetoothDevice = "HC-06";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread BluetoothThread;

    private String rxData;
    private BluetoothListener listener;

    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopBluetoothThread;

    // constructors
    public Bluetooth(){
        rxData = "";
        stopBluetoothThread = true;
        this.listener = null;
    }


    // methods
    private void findDevice() throws BluetoothException{
        /*
        This methods checks if a bluetooth module is available. If so it searches the external sensor in paired devices.
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            throw new BluetoothException("Warning: No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            throw new BluetoothException("Warning: Bluetooth is disabled");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            boolean device_found = false;
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(BluetoothDevice))
                {
                    mmDevice = device;
                    device_found = true;
                    break;
                }
            }
            if(!device_found){
                throw new BluetoothException("Warning: device " + BluetoothDevice + " is not paired");
            }
        }
        else{
            throw new BluetoothException("Warning: No paired devices found");
        }
    }

    private void readData() {
        /*
        This method starts a thread receiving the serial data from the external sensor. These values were passed back through a Handler.
         */

        final Handler mainHandler = new Handler();
        final byte delimiter = 10; //  ASCII code for a newline character

        stopBluetoothThread = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        BluetoothThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopBluetoothThread)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    mainHandler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            setRxData(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        stopBluetoothThread = true;
                    }
                }
            }
        });

        BluetoothThread.start();
    }

    private void establishConnection() throws IOException{
        /*
        this methods starts the connection to the external sensor.
         */
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
    }

    public void openConnection() throws BluetoothException{
        /*
        One method to rule them all.
         */

        try{
            findDevice();
        } catch (BluetoothException e){
            throw e;
        }

        try {
            establishConnection();
        } catch (IOException e){
            throw new BluetoothException("Error: can't open bluetooth connection");
        }

        try {
            readData();
        } catch (Exception e){
            throw new BluetoothException("Error: can't read bluetooth data");
        }
    }

    public void sendData(String msg) throws IOException {
        /*
        This method can send serial data to the external sensor.
         */

        msg += "\n";
        mmOutputStream.write(msg.getBytes());
    }

    public void closeConnection() throws BluetoothException {
        /*
        This method closes the connection to the external sensor.
         */

        try {
            stopBluetoothThread = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (IOException e){
            throw new BluetoothException("Error: can't close bluetooth connection");
        }
    }

    public boolean isConnected(){
        /*
        This method makes it possible to check whether the bluetooth thread is running or not
         */
        if(stopBluetoothThread){
            return false;
        }
        return true;
    }

    // methods for rxData calling listener

    public void setBluetoothListener(BluetoothListener listener){
        this.listener = listener;
    }

    private void setRxData(String data){
        rxData = data;
        if (listener != null){
            listener.onChange(rxData);
        }
    }

    private String getRxData(){
        return rxData;
    }


}



