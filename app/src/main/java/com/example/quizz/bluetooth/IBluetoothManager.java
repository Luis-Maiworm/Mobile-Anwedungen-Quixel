package com.example.quizz.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.quizz.exceptions.BluetoothException;

import java.util.ArrayList;

public interface IBluetoothManager {


    ArrayList<BluetoothDevice> getmDevices();

    /**
     * Sets up the BluetoothAdapter and some permissions.
     * @throws BluetoothException if the device doesn't support Bluetooth.
     */
    void init() throws BluetoothException;

    /**
     * Asks to enable Bluetooth on the user's device.
     */
    void enableBt();

    /**
     * Asks to disable Bluetooth on the user's device.
     */
    void disableBt();

    /**
     * Asks to make the device visible for other Bluetooth devices for 300 seconds.
     */
    void visible();

    /**
     * Discovers for nearby bluetooth devices.
     */
    void discover();

    /**
     * Pairs with another bluetooth device. Also cancels the discovery, if running (saves resources).
     * @param mDevice the device to pair with.
     * @throws BluetoothException if no device is selected or the {@code mDevice} is null.
     */
    void pair(BluetoothDevice mDevice) throws BluetoothException;

    /**
     * Builds a connection to the paired device.
     * Only works if it's paired with another device.
     */
    void connect();

    /**
     * Writes a String to the OutputStream, which can be accessed by the connected device.
     * @param input
     */
    void write(String input);

    /**
     * Writes an object to the OutputStream, which can be accessed by the connected device.
     * @param o
     */
    void write(Object o);

    /**
     * Reads the InputStream, which delivers the information from the OutputStream of connected devices.
     * @return
     */
    String read();

    /**
     * Unregisters all BroadCastReceiver.
     */
    void terminate();


}
