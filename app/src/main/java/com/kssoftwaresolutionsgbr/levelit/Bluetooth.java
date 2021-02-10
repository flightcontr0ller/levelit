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
    Thread workerThread;
    private String rxData;
    public String debugMsg;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopThread;

    private ChangeListener listener;

    // constructors
    public Bluetooth(){
        rxData = "";
        debugMsg = "";
    }

    // methods
    public void findBT() throws BluetoothException{
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

    public void openBT() throws BluetoothException{
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

        debugMsg = "Serial bluetooth communication opened";
    }

    private void readData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopThread = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
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

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            setRxData(data);
                                            // incoming text is stored in string "data"
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
                        stopThread = true;
                        try {
                            throw e;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
        workerThread.start();
    }

    public void sendData() throws IOException {
        String msg = "message";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        debugMsg = "Data is sent to bluetooth device";
    }

    public void closeBT() throws BluetoothException {
        try {
            stopThread = true;
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
}



