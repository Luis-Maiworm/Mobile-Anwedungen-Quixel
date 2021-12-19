package com.example.quizz.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnection {
    private static final String TAG = "BluetoothConnection";
    private static final String NAME = "Quixel";
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");    // todo ?

    private final BluetoothAdapter bluetoothAdapter;
    Context mContext;

    private AcceptThread mAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public String getIncomingMessage() {
        return this.incomingMessage;
    }

    String incomingMessage;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public BluetoothConnection(Context context) {
        mContext = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();

    }

    private class AcceptThread extends Thread {
        //local server socket
        private final BluetoothServerSocket mmServerSocket;


        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;

            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread Running");

            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.

            try{
                Log.d(TAG, "run: RFCOM server socket start..");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection");

            } catch(IOException e){
                Log.e(TAG, "AcceptThread: IOEXception: " + e.getMessage());
            }

            if(socket != null){
                connected(socket, mmDevice);

                try {
                    mmServerSocket.close();     //todo ? necesary?
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "End mAcceptThread");
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;


        public ConnectThread(BluetoothDevice device, UUID uuid) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            Log.d(TAG, "ConnectThread: started");

            mmDevice = device;
            deviceUUID = uuid;

        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");


            try {

                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID );
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;


            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            // connected opens a new connectedThread

            connected(mmSocket, mmDevice);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }


    /**
     *
     */
    public synchronized void start(){
        Log.d(TAG, "start");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mAcceptThread == null){
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }


    /**
     *  AcceptThread starts and sits waiting for a connection.
     *  Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     * @param device
     * @param uuid
     */
    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: Started.");


        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);



        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss progdialog when connection is established
            try {
                mProgressDialog.dismiss();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }


            mmInStream = tmpIn;
            mmOutStream = tmpOut;



            // this code should be executed, if its determined what format ur reading from... if its an object, this code should be executed
            // or: execute it always. it doesnt change the way
            try{
                oos = new ObjectOutputStream(mmOutStream);
                oos.flush();
                ois = new ObjectInputStream(mmInStream);

            } catch (IOException e){
                Log.e(TAG, "Error");
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }

            //todo another stream (object in/output stream)
            //todo switch after Object type (parameter), so the program can send strings, bytes OR objects as it pleases -> generic
        }



        public void run(){
            byte[] buffer  = new byte[1024]; //buffer store for the stream

            int bytes; //bytes returned by "read()"

            while(true){

                try{


                    //todo check how long this while is being executed... -> check hows it acting (is it consistently reading?
                    // and if it read some bytes, and some more are being added, what happens?
                    // test it.
                    bytes = mmInStream.read(buffer);
                    incomingMessage = new String(buffer, 0, bytes);

                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e){
                    Log.e(TAG, "write: input stream is disconnected. " + e.getMessage());
                    break;
                }
            }

        }
        //call from the main activity to send data from the remote device
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: writing to outputstream");

            try{
                mmOutStream.write(bytes);
            } catch (IOException e){
                Log.e(TAG, "write: error writing to output stream. " + e.getMessage());
            }
        }


        public void write(Serializable object) {
            Log.d(TAG, "writing Object to outputstream");

            try{
                oos.writeObject(object);

            } catch (IOException e){
                Log.e(TAG, "write: error writing object to output stream. " + e.getMessage());
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            } catch(IOException e){

            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice){
        Log.d(TAG, "connected: Starting.");

        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    //write to connectedThread in unsynchronized manner
    public void write(byte[] out){
        //temporary object
        ConnectedThread r;

        //synchronize copy of ConnectedThread
        Log.d(TAG, "write: write called.");
        //perform the write
        mConnectedThread.write(out);
    }
}