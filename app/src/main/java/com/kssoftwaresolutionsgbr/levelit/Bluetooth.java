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

    // fields
    public static final String BluetoothDevice = "HC-06";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread BluetoothThread;

    private String rxData;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopBluetoothThread;
    private boolean IsBtThreadRunning;
    private ChangeListener listener;

    // constructors
    public Bluetooth(){
        rxData = "";
    }

    // methods
    public void findDevice() throws BluetoothException{
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

    public void connectDevice() throws BluetoothException{
            try {
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
            } catch (IOException e){
                throw new BluetoothException("Error: can't open bluetooth connection");
            }

            try {
                readData();
            } catch (Exception e){
                throw new BluetoothException("Error: can't read bluetooth data");
            }

            // "Serial bluetooth communication opened";
    }

    private void readData() {

        /*
        Thread explanation:
        multiple thread object can run parallel
        Runnables are packages of work, which can be passed as an argument
        Handler is an object to communicate between threads
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
                    finally {
                        if(!stopBluetoothThread){
                            mainHandler.post(new Runnable()
                            {
                                public void run()
                                {
                                    setIsBtThreadRunning(true);                                }
                            });
                        }
                        else {
                            mainHandler.post(new Runnable()
                            {
                                public void run()
                                {
                                    setIsBtThreadRunning(false);
                                }
                            });
                        }
                    }
                }
            }
        });

        BluetoothThread.start();
    }

    public void sendData() throws IOException {
        String msg = "message";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        // "Data is sent to bluetooth device"
    }

    public void closeBT() throws BluetoothException {
        try {
            stopBluetoothThread = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (IOException e){
            throw new BluetoothException("Error: can't close bluetooth connection");
        }
    }

    // methods for rxData calling listener
    public String getRxData() {
        return rxData;
    }

    private void setRxData(String data) {
        this.rxData = data;
        if (listener != null) listener.onChange();
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

    public boolean getIsBtThreadRunning(){
        return IsBtThreadRunning;
    }

    private void setIsBtThreadRunning(boolean data){
        IsBtThreadRunning = data;
    }
}



