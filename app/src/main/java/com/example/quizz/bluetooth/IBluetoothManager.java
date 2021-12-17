package com.example.quizz.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.quizz.exceptions.BluetoothException;

import java.util.ArrayList;

public interface IBluetoothManager {


    ArrayList<BluetoothDevice> getmDevices();

    void init() throws BluetoothException;

    void enableBt();

    void disableBt();

    void visible();

    void discover();

    void pair() throws BluetoothException;

    void connect();

    void write(String input);

    String read();

    void terminate();




}
