package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.gameLogic.StatisticsAnalyser;
import com.example.quizz.questionManager.Question;
import java.util.ArrayList;


/**
 * Final Screen which shows up after finishing a game. Displays different statistics and asks if the
 * user wants to go back to the home menu, start a new game or choose another category.
 *
 */
public class EndscreenActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private Button goHome, goToCategoryScreen, playAgain;
    private TextView correctLabel, incorrectLabel, congratsMessage;
    // Data
    private int score, correct, incorrect;
    private Player player;
    private ArrayList<Question> answeredQuestions;
    // Intents
    private Intent goToHome, goToCategory;
    private StatisticsAnalyser statisticsAnalyser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        getData();
        setup();
        analyseGamePerformance();
    }

    /**
     * Sets up the View and Listeners
     */
    private void setup() {
        // Setup View
        congratsMessage = findViewById(R.id.congratsMessageLabel);
        goHome = findViewById(R.id.goToHomeBtnMP);
        goToCategoryScreen = findViewById(R.id.goToCategoryBtn);
        playAgain = findViewById(R.id.AgainBtn);
        correctLabel = findViewById(R.id.finalScoreLabel1);
        incorrectLabel = findViewById(R.id.finalScoreLabel2);

        goToCategoryScreen.setVisibility(View.INVISIBLE);
        playAgain.setVisibility(View.INVISIBLE);
        // Listener
        goHome.setOnClickListener(this);
        goToCategoryScreen.setOnClickListener(this);
        playAgain.setOnClickListener(this);
        // Init View
        score = getIntent().getIntExtra("score", 0);
        correctLabel.setText(getString(R.string.finalScoreCorrect, correct));
        incorrectLabel.setText(getString(R.string.finalScroreIncorrect, incorrect));
        congratsMessage.setText(getString(R.string.conMessageLabel, player.getPlayerName(), score));
    }

    /**
     * Gets the data from the game loop
     */
    private void getData() {
        // Get Data
        correct = getIntent().getIntExtra("correct", 0);
        incorrect = getIntent().getIntExtra("incorrect", 0);
        player = (Player) getIntent().getSerializableExtra(Constants.playerConstant);
        answeredQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("q");
    }

    /**
     * Starts the StatisticsAnalyser and processes the answered Questions so that the data can be
     * saved
     */
    private void analyseGamePerformance() {
        statisticsAnalyser = new StatisticsAnalyser(player);
        statisticsAnalyser.afterRound(answeredQuestions);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.goToHomeBtnMP:
                // Intent
                goToHome = new Intent(this, MainMenuActivity.class);
                goToHome.putExtra(Constants.playerConstant, player);
                startActivity(goToHome);
                finish();
                break;
            case R.id.goToCategoryBtn:
                goToCategory = new Intent(this, ChooseCategoryActivity.class);
                goToCategory.putExtra(Constants.playerConstant, player);
                finish();
                startActivity(goToCategory);
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}