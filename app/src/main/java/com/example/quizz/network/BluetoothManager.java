package com.example.quizz.network;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.activity.result.ActivityResultCallback;
import androidx.core.content.ContextCompat;
import com.example.quizz.exceptions.BluetoothException;
import com.example.quizz.view.MainMenuActivity;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothManager implements IBluetoothManager{

    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private static final String TAG = "BluetoothManager";

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
    private BluetoothDevice mDevice;
    private BluetoothConnection btConnection;
    private BluetoothAdapter btAdapter;
    Context c;


    public BluetoothManager(Context c){
        this.c = c;
    }

    @Override
    public ArrayList<BluetoothDevice> getmDevices() {
        return this.mDevices;
    }

    @Override
    public void init() throws BluetoothException {
        checkPermission();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        c.registerReceiver(mBroadcastReceiver4, filter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null) {
            throw new BluetoothException("Bluetooth is not supported on your device.");
        }
    }

    @Override
    public void enableBt() {
        if(!btAdapter.isEnabled()){
            Log.d(TAG, ": bluetooth enabled");
            btAdapter.enable();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            c.startActivity(enableBtIntent);

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            c.registerReceiver(mBroadcastReceiver1, btIntent);
        }
    }

    @Override
    public void disableBt() {
        if(btAdapter.isEnabled()){
            Log.d(TAG, ": bluetooth disabled");
            btAdapter.disable();

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            c.registerReceiver(mBroadcastReceiver1, btIntent);
        }
    }

    @Override
    public void visible() {            //context?
        Log.d(TAG, ": tries to become visible for other devics");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        c.startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        c.registerReceiver(mBroadcastReceiver2, intentFilter);

    }

    @Override
    public void discover() {
        Log.d(TAG, ": try to discover");

        if(btAdapter.isDiscovering()){          //restart discovering
            btAdapter.cancelDiscovery();

            Log.d(TAG, ": restart! discovering");

            checkPermissions();

            btAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            c.registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

        if(!btAdapter.isDiscovering()){

            Log.d(TAG, ": start discovering");
            checkPermissions();

            btAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            c.registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }
    }

    @Override
    public void pair(BluetoothDevice device) throws BluetoothException {

        Log.d(TAG, ": cancels discovery, and creates bond with device");
        btAdapter.cancelDiscovery();

        mDevice = device;

        if(mDevice == null){
            throw new BluetoothException("No device selected"); //todo richtige message?
        }

        mDevice.createBond();
        btConnection = new BluetoothConnection(c);

        //todo intentfilter + receiver 4?
    }

    @Override
    public void connect() {
        //todo add log
        //works if paired
        Log.d(TAG, ": connects to paired device");
        btConnection.startClient(mDevice, MY_UUID);
    }

    @Override
    public void write(String input) {
        byte[] bytes = input.getBytes(Charset.defaultCharset());
        btConnection.write(bytes);
    }

    @Override
    public void write(Object o) {
        btConnection.write(o);
    }

    @Override
    public String read() {
        return btConnection.getIncomingMessage();
    }

    @Override
    public void terminate() {
        unregisterBroadcastReceiver(mBroadcastReceiver1);
        unregisterBroadcastReceiver(mBroadcastReceiver2);
        unregisterBroadcastReceiver(mBroadcastReceiver3);
        unregisterBroadcastReceiver(mBroadcastReceiver4);

    }

    private void unregisterBroadcastReceiver(BroadcastReceiver br){
        try {
            c.unregisterReceiver(br);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }


    private void checkPermissions(){
        Activity activity = (Activity) c;                           // cast ok?
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = activity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += activity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {


                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }

        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    public void checkPermission(){

        ActivityResultCallback test;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Activity activity = (Activity) c;
            int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;

            activity.requestPermissions(new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else {
            return;
        }

    }






    // add broadcast receiver to send and read methods?


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(btAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, btAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice mmDevice = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                if(mmDevice.getName() != null && !mDevices.contains(mmDevice)){         // checks if the name of the found bluetooth device is == null and if the list already contains the found device (avoids duplicates)
                    mDevices.add(mmDevice);

                    /*
                    if(mmDevice.getName == null){
                        mmDevice.getName() = "";
                    }
                     */
                    //todo should null names be added to the list?
                }

                Log.d(TAG, "onReceive: " + mmDevice.getName() + ": " + mmDevice.getAddress());
                Log.d(TAG, "size of list " + mDevices.size());

                //    mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                //    lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mmDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mmDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mDevice = mmDevice;
                }
                //case2: creating a bond
                if (mmDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mmDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };












}
