package com.example.quizz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.quizz.data.playerData.Player;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import network.BluetoothConnection;

public class MainActivity extends AppCompatActivity {

    private Button openStats;
    private Player p1;

    private static final String TAG = "MainActivity";

    BluetoothAdapter bluetoothAdapter;
    BluetoothConnection bluetoothConnection;

    ListView lvDevices;

    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mDevice;

    public ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    //   public DeviceListAdapter mDeviceListAdapter;  //-> view der Bluetooth devices

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openStats = (Button) findViewById(R.id.statsBtn);
        openStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothPairing();
            }
        });

        Button discover = (Button) findViewById(R.id.discover);
        Button enable_disable = (Button) findViewById(R.id.enable_disable);
        Button msg = (Button) findViewById(R.id.msg);
        Button discoverable = (Button) findViewById(R.id.discoverable);
        Button fetch = (Button) findViewById(R.id.fetch);
        Button send = (Button) findViewById(R.id.send);


        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });

        enable_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //whoever does this, becomes the client
                startConnection();
            }
        });

        discoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisable_discovering();
            }
        });


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Gold");
                builder.setMessage(bluetoothConnection.getIncomingMessage());
                System.out.println(bluetoothConnection.getIncomingMessage());
                builder.show();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nabend = "{\"response_code\":0,\"results\":[{\"category\":\"Science: Computers\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"Moore&#039;s law originally stated that the number of transistors on a microprocessor chip would double every...\",\"correct_answer\":\"Year\",\"incorrect_answers\":[\"Four Years\",\"Two Years\",\"Eight Years\"]},{\"category\":\"Geography\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"Which of these island countries is located in the Caribbean?\",\"correct_answer\":\"Barbados\",\"incorrect_answers\":[\"Fiji\",\"Maldives\",\"Seychelles\"]},{\"category\":\"History\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"During the Wars of the Roses (1455 - 1487) which Englishman was dubbed &quot;the Kingmaker&quot;?\",\"correct_answer\":\"Richard Neville\",\"incorrect_answers\":[\"Richard III\",\"Henry V\",\"Thomas Warwick\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Who is the protagonist in Dead Rising (2006)?\",\"correct_answer\":\"Frank West\",\"incorrect_answers\":[\"Chuck Greene\",\"John North\",\"Jason Grey\"]},{\"category\":\"Mythology\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"In Norse mythology, what is the name of the serpent which eats the roots of the ash tree Yggdrasil?\",\"correct_answer\":\"Nidhogg\",\"incorrect_answers\":[\"Bragi\",\"Odin\",\"Ymir\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"In the game &quot;Persona 4&quot;, what is the canonical name of the protagonist?\",\"correct_answer\":\"Yu Narukami\",\"incorrect_answers\":[\"Chino Mashido\",\"Tunki Sunada\",\"Masaki Narinaka\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"In the original DOOM (1993) which of the following is NOT a cheat code?\",\"correct_answer\":\"IDCLIP\",\"incorrect_answers\":[\"IDFA\",\"IDDQD\",\"IDSPISPOPD\"]},{\"category\":\"Entertainment: Television\",\"type\":\"boolean\",\"difficulty\":\"medium\",\"question\":\"In &quot;Star Trek&quot;, Klingons are commonly referred to as &quot;Black Elves&quot;.\",\"correct_answer\":\"False\",\"incorrect_answers\":[\"True\"]},{\"category\":\"General Knowledge\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"When did the website &quot;Facebook&quot; launch?\",\"correct_answer\":\"2004\",\"incorrect_answers\":[\"2005\",\"2003\",\"2006\"]},{\"category\":\"History\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"Before the American colonies switched to the Gregorian calendar in 1752, on what date did their new year start?\",\"correct_answer\":\"March 25th\",\"incorrect_answers\":[\"June 1st\",\"September 25th\",\"December 1st\"]}]}";
                byte[] bytes = nabend.getBytes(Charset.defaultCharset());
                bluetoothConnection.write(bytes);
            }
        });


        //   lvDevices = (ListView) findViewById(R.id.lvNewDevices);

        //"broadcasts when bond state changes"
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver4, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // Opens Statistics Activity on "Statistics" Button click
    public void openStatsMenu() {
        Intent toStats = new Intent(MainActivity.this, Statistics.class);

        toStats.putExtra("Player", p1);

        startActivity(toStats);
    }


    public void onClick(View v) {
        if (v.getId() == R.id.multiplayerBtn) {

           /* if(!bluetoothAdapter.isEnabled()){
                enableDisableBT();
            }*/

            enableDisableBT();

            enableDisable_discovering();        // asks for bluetooth to turn on and to be visible for the next 300 secs

            discover();                         // discovers bluetooth devices, puts them into the ArrayList mDevices

            // discover();
        }

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

        //  AcceptThread t1 = new AcceptThread(bluetoothConnection);

    }

    public void bluetoothPairing(){

        bluetoothAdapter.cancelDiscovery();

        for(BluetoothDevice device : mDevices) {

            String deviceName = device.getName();
            String deviceAddress = device.getAddress();


            if(deviceName!=null){   //viele bt gerÃ¤te haben ne null value als name

                // if (deviceName.equals("DESKTOP-K724GJC")) {
                if (deviceName.equals("TECNO Pouvoir 3")) {
                    Log.d(TAG, "Tecno Found, trying to pair");

                    mDevice = device;
                    mDevice.createBond();

                    //       startConnection(); //starts connection to the paired device (laptop)

                    bluetoothConnection = new BluetoothConnection(MainActivity.this);
                }
                if (deviceName.equals("Galaxy S9")) {
                    Log.d(TAG, "S9 found");

                    mDevice = device;
                    mDevice.createBond();

                    //       startConnection(); //starts connection to the paired device (laptop)

                    bluetoothConnection = new BluetoothConnection(MainActivity.this);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "on destroy: called");
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);

    }

    public void enableDisableBT(){
        if(bluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!bluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(bluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            bluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }

    //can only work if ur paired
    public void startConnection(){
        startBTConnection(mDevice, MY_UUID);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBtConnection: Initializing RFCOM Bluetooth connection");

        bluetoothConnection.startClient(device, uuid);
    }

    public void enableDisable_discovering(){
        Log.d(TAG, "enabledisable_discovering: making device discoverable for 300sec");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void discover(){
        Log.d(TAG, "discover: looking for unpaired devices");

        if(bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "discover: canceling discovery");

            //check bt permissions in manifest
            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

        if(!bluetoothAdapter.isDiscovering()){
            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * Required for apps API23+
     * Needs to be done.
     * Doesn't execute if API is > LOLLIPOP
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

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
                mDevices.add(mmDevice);
                Log.d(TAG, "onReceive: " + mmDevice.getName() + ": " + mmDevice.getAddress());
                Log.d(TAG, "size of list" + mDevices.size());

                //      mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
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
                //case2: creating a bone
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