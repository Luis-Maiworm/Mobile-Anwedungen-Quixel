package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;


/**
 * Final Screen which shows up after finishing a game. Displays different statistics and asks if the
 * user wants to go back to the home menu, start a new game or choose another category.
 *
 */
public class EndscreenActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private Button goHome, goToCategoryScreen;
    private TextView scoreLabel, congratsMessage;
    // Data
    private int score;
    private Player player;
    private String testName;
    // Intents
    private Intent goToHome, goToCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);


        // Setup View
        congratsMessage = findViewById(R.id.congratsMessageLabel);
        goHome = findViewById(R.id.goToHomeBtn);
        // Listener
        goHome.setOnClickListener(this);

        // Init View
        score = getIntent().getIntExtra("score", 0);
        congratsMessage.setText(getString(R.string.conMessageLabel, testName, score));












    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.goToHomeBtn:
                goToHome = new Intent(this, MainMenuActivity.class);
                goToHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(goToHome, 0);
                // goToHome.putExtra("Player", player);

                break;
            case R.id.goToCategoryBtn:
                break;
            default:
                break;


        }
    }

    /**
     * Prevents the user from going back to the game loop after finishing the game
     */
    @Override
    public void onBackPressed() {
    }
}