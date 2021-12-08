package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;


public class StatisticsActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent getPlayer = getIntent();
        Player player = (Player) getPlayer.getSerializableExtra("Player");

        TextView textView = (TextView) findViewById(R.id.wonStats);
        textView.setText("Games won: " + player.getPlayerID());





    }


}