package com.example.quizz.view;

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

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    BluetoothManager bManager;
    Button enable, disable, discover, pair, connect, visible, refresh;

    SwipeRefreshLayout swipeLayout;

    RecyclerView recyclerView;
    BluetoothDevicesRVAdapter rvAdapter;
    GridLayoutManager gridLayoutManager;
 //   List<BluetoothDevice> mDevices;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);



        bManager = new BluetoothManager(this);

        initVariables();


        recyclerView = findViewById(R.id.recyclerViewBluetooth);
        rvAdapter = new BluetoothDevicesRVAdapter(this, bManager.getmDevices());

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


    //    bManager.enableBt();
     //   bManager.discover();



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

                rvAdapter.notifyDataSetChanged();

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

        bManager.terminate();
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
            case R.id.pair:
                try {
                    bManager.pair();
                } catch (BluetoothException e){
                    System.out.println(e.getMessage()); //todo alertdialog
                }
                break;
            case R.id.connect:
                bManager.connect();
                break;
            case R.id.visible:
                bManager.visible();
                break;
            case R.id.refresh:                        //todo nicht über button, sonder nach unten swipen
                rvAdapter.notifyDataSetChanged();
                System.out.println("lüppt");
                break;

        }
    }





}
