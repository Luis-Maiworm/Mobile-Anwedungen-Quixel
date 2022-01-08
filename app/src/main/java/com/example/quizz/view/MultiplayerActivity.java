package com.example.quizz.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.exceptions.QueryException;
import com.example.quizz.gameLogic.gamemodes.Gamemode_mp;
import com.example.quizz.network.BluetoothConnection;
import com.example.quizz.network.BluetoothManager;
import com.example.quizz.exceptions.BluetoothException;
import com.example.quizz.network.Wrapper;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;
import com.example.quizz.viewControl.BluetoothDevicesRVAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    BluetoothManager bManager;
    Button connect, refresh, write, Player1, Player2;
    private CountDownTimer timer;
    boolean flag;
    SwipeRefreshLayout swipeLayout;
    private Player player;
    RecyclerView recyclerView;
    BluetoothDevicesRVAdapter rvAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        player = (Player)getIntent().getSerializableExtra(Constants.playerConstant);
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
        bManager.enableBt();
        bManager.visible();
        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//



    }

    public void initVariables(){
        swipeLayout = findViewById(R.id.swipeLayout);
        connect = findViewById(R.id.connect);
        refresh = findViewById(R.id.discover);
        Player1 = findViewById(R.id.Player1);
        Player2 = findViewById(R.id.Player2);
        write = findViewById(R.id.play);
        write.setVisibility(View.INVISIBLE);

        Player1.setOnClickListener(this);
        Player2.setOnClickListener(this);
        write.setOnClickListener(this);
        connect.setOnClickListener(this);
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
    // sollte eigentlich an allen neu hinzugekommenen positionen updaten / notifien...
    public void notifyAlgo(){
        List<BluetoothDevice> temp = bManager.getmDevices();

        for(int i = prevState.size(); prevState.size() < temp.size(); i++){
            if(i == 0) return;
            rvAdapter.notifyItemChanged(i-1);
        }
        prevState = temp;
    }


    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.connect:
                bManager.connect();
                break;
            case R.id.Player1:
                flag = true;
                write.setVisibility(View.VISIBLE);
                break;
            case R.id.Player2:
                new AsyncCaller().execute();
                break;
            case R.id.discover:
                bManager.discover();
                timer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        refresh();
                    }
                }.start();
                break;
            case R.id.play:
                if (flag) {
                    try {
                        start();
                    } catch (IOException | QueryException e) {
                        e.printStackTrace();
                    }
                    player.setFlag(flag);
                    Wrapper wrap = new Wrapper();
                    wrap.setqList(qList);
                    wrap.setPlayer(player);
                    Intent intent = new Intent(this, Gamemode_mp.class);
                    intent.putExtra("flag", wrap);
                    startActivity(intent);
                }
        }
    }
    ArrayList<Question> qList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() throws IOException, QueryException {
        QuestionManager qm = new QuestionManager();

        qList = (ArrayList<Question>) qm.getApiData(3, -1, "", "boolean");
        //qList.stream();
        System.out.println("liste vorher" + qList.get(0).getQuestion());
        Wrapper wrap = new Wrapper();
        wrap.setqList(qList);
        bManager.write(wrap);
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Wrapper>
    {
        //ProgressDialog pdLoading = new ProgressDialog(MultiplayerActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            //pdLoading.setMessage("\tLoading...");
            //pdLoading.show();
        }
        @Override
        protected Wrapper doInBackground(Void... params) {
            Wrapper wrap = new Wrapper();
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            while(!flag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    wrap = bManager.readQuestions();
                    if (wrap.getqList()!= null){return wrap;}
                }
                catch(NullPointerException e){ e.printStackTrace();
                }

            }

            return wrap;
        }

        @Override
        protected void onPostExecute(Wrapper wrap) {
            super.onPostExecute(wrap);

            //this method will be running on UI thread
            if(wrap != null){
                flag = false;
                player.setFlag(flag);
                wrap.setPlayer(player);
                Intent intent = new Intent(MultiplayerActivity.this, Gamemode_mp.class);
                intent.putExtra("flag", wrap);
                startActivity(intent);
            }
            //pdLoading.dismiss();
        }

    }

}