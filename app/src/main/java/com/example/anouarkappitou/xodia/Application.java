package com.example.anouarkappitou.xodia;

import android.bluetooth.BluetoothSocket;

import com.example.bluetoothlib.Bluetooth;

/**
 * Created by anouarkappitou on 8/1/17.
 */

public class Application extends android.app.Application {

    private BluetoothSocket _socket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void set_socket(BluetoothSocket socket )
    {
        _socket = socket;
    }

    public BluetoothSocket get_socket(){
        return _socket;
    }
}
