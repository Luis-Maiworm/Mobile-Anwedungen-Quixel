package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;

/**
 * Main Menu Activity
 */
public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button openSingleplayer, openMultiplayer, openStats;
    // Global Player
    private Player p1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Testing
        p1 = new Player("Test", 2);

        // View Setup
        openSingleplayer = findViewById(R.id.singleplayerBtn);
        openMultiplayer = findViewById(R.id.multiplayerBtn);
        openStats = findViewById(R.id.statsBtn);
        // Button openSettings = findViewById(R.id.statsBtn);
        openSingleplayer.setOnClickListener(this);
        openMultiplayer.setOnClickListener(this);
        openStats.setOnClickListener(this);
        // openSettings.setOnClickListener(this);
    }

    // Menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.singleplayerBtn:
                startSingleplayer();
                break;
            case R.id.multiplayerBtn:
                startMultiplayer();
                break;
            case R.id.statsBtn:
                openStatsMenu();
                break;
            default:
                break;
        }
    }


    public void openStatsMenu() {
        Intent toStats = new Intent(MainMenuActivity.this, StatisticsActivity.class);
        toStats.putExtra("Player", p1);
        startActivity(toStats);
    }
    public void openSettingsMenu() {
    }
    public void startSingleplayer() {
        Intent toSingleplayer = new Intent(MainMenuActivity.this, ChooseGamemodeActivity.class);
        toSingleplayer.putExtra("Player", p1);
        startActivity(toSingleplayer);
    }
    public void startMultiplayer() {
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you really want to quit?");
        builder.setPositiveButton(R.string.quitYes, (dialog, id) -> {
            finish();
        });
        builder.setNegativeButton(R.string.QuitCancel, (dialog, id) -> {
        });
        builder.show();
    }
}

