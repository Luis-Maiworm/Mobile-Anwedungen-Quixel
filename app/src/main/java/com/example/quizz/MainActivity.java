package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizz.data.playerData.Player;


public class MainActivity extends AppCompatActivity {

    private Button openStats;
    private Player p1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1 = new Player(2);

        openStats = (Button) findViewById(R.id.statsBtn);
        openStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatsMenu();
            }
        });

    }

    // Opens Statistics Activity on "Statistics" Button click
    public void openStatsMenu() {
        Intent toStats = new Intent(MainActivity.this, Statistics.class);

        toStats.putExtra("Player", p1);

        startActivity(toStats);
    }
}