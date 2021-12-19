package com.example.quizz.view;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quizz.R;
import com.example.quizz.bluetooth.BluetoothManager;
import com.example.quizz.exceptions.BluetoothException;
import com.example.quizz.viewControl.BluetoothDevicesRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    BluetoothManager bManager;
    Button enable, disable, discover, pair, connect, visible, refresh;

    SwipeRefreshLayout swipeLayout;

    RecyclerView recyclerView;
    BluetoothDevicesRVAdapter rvAdapter;
 //   GridLayoutManager gridLayoutManager;
 //   List<BluetoothDevice> mDevices;
    LinearLayoutManager linearLayoutManager;

    //todo observer einbauen -> resettet automatisch mDevices und notifyItemChanged() wenn devices sich verändert

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        bManager = new BluetoothManager(this);

        initVariables();

        recyclerView = findViewById(R.id.recyclerViewBluetooth);
        rvAdapter = new BluetoothDevicesRVAdapter(this, bManager.getmDevices(), bManager);


      //  gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
      //  recyclerView.setLayoutManager(gridLayoutManager);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(rvAdapter);

        try {
            bManager.init();
        } catch (BluetoothException e) {
            e.printStackTrace();
        }

    }

    public void initVariables(){
        swipeLayout = findViewById(R.id.swipeLayout);
        enable = findViewById(R.id.enable);
        disable = findViewById(R.id.disable);
        discover = findViewById(R.id.discover);
        pair = findViewById(R.id.pair);
        connect = findViewById(R.id.connect);
        visible = findViewById(R.id.visible);
        refresh = findViewById(R.id.refresh);

        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        discover.setOnClickListener(this);
        pair.setOnClickListener(this);
        connect.setOnClickListener(this);
        visible.setOnClickListener(this);
        refresh.setOnClickListener(this);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);           //todo maybe loop the notify data change? -> mehrere notifies pro "refresh"
                    }
                }, 1000);

            }
        });
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();

        bManager.disableBt();
        bManager.terminate();
    }

    public void refresh(){
        rvAdapter.notifyDataSetChanged(); //evtl mDevices ausgeben lassen, und alle die neu hinzugekommen sind einzeln eintragen
        //notifyAlgo();
    }



    List<BluetoothDevice> prevState = new ArrayList<>();
    /**
     * Algo to avoid using the notifyDataSetChanged() method.
     */
    public void notifyAlgo(){
        List<BluetoothDevice> temp = bManager.getmDevices();

        for(int i = prevState.size(); prevState.size() < temp.size(); i++){
            if(i == 0) return;
            rvAdapter.notifyItemChanged(i-1);
        }
        prevState = temp;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.enable:
                bManager.enableBt();
                break;
            case R.id.disable:
                bManager.disableBt();
                break;
            case R.id.discover:
                bManager.discover();
                break;
            case R.id.connect:
                bManager.connect();
                break;
            case R.id.visible:
                bManager.visible();
                break;
            case R.id.refresh:                        //todo nicht über button, sonder nach unten swipen
                refresh();
                break;

        }
    }






}
