package com.example.inteligeantav4;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnect
{


    public static final String TAG = BluetoothConnect.class.getSimpleName();
    private final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static InputStream in = null;
    private static OutputStream out = null;
    private BluetoothActivity bluetoothActivity;

    public BluetoothConnect(BluetoothActivity bluetoothActivity)
    {
        this.bluetoothActivity = bluetoothActivity;
    }

    public static boolean connect(final String strMacAddress)
    {
        try
        {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = btAdapter.getRemoteDevice(strMacAddress);
            BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            if(btSocket.isConnected())
            {
                in = btSocket.getInputStream();
                out = btSocket.getOutputStream();
                return true;
            }
        }catch (IOException ex) {}

        return false;
    }

    public static void send(String strMessage)
    {
        Log.d(TAG, "send: " + strMessage);
        strMessage += "#";
        byte[] msgBuffer = strMessage.getBytes();
        try
        {
            out.write(msgBuffer);
            out.flush();
        }
        catch (IOException e) {}
    }

    public static String read() {
        StringBuilder sInput = new StringBuilder();
        try
        {
            while(in.available() > 0)
            {
                char input = (char)in.read();
                sInput.append(input);
                if(input=='\n')
                    break;
            }
        }catch (IOException exp){}
        return sInput.toString();
    }
}
