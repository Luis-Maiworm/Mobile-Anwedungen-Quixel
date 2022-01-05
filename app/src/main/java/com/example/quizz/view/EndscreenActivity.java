package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.Statistics;
import com.example.quizz.gameLogic.PlayerManager;
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
    private TextView scoreLabel, congratsMessage;
    // Data
    private int score;
    private Player player;
    private String testName;
    private ArrayList<Question> answeredQuestions;
    // Intents
    private Intent goToHome, goToCategory;
    private StatisticsAnalyser statisticsAnalyser;
    // SharedPreferences
    private SharedPreferences pref;
    private  SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);


        // Setup View
        congratsMessage = findViewById(R.id.congratsMessageLabel);
        goHome = findViewById(R.id.goToHomeBtn);
        goToCategoryScreen = findViewById(R.id.goToCategoryBtn);
        playAgain = findViewById(R.id.AgainBtn);

        goToCategoryScreen.setVisibility(View.INVISIBLE);
        playAgain.setVisibility(View.INVISIBLE);
        // Listener
        goHome.setOnClickListener(this);
        goToCategoryScreen.setOnClickListener(this);
        playAgain.setOnClickListener(this);

        // Init View
        score = getIntent().getIntExtra("score", 0);
        congratsMessage.setText(getString(R.string.conMessageLabel, testName, score));

        // Get Data
        player = (Player) getIntent().getSerializableExtra(Constants.playerConstant);
        System.out.println("PLAYER END" + player.getPlayerName());
        answeredQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("q");
        System.out.println("QUESTION: " + answeredQuestions.get(0).getQuestion() + "   ISCORRECT:" + answeredQuestions.get(0).isCorrect() +
                answeredQuestions.get(0).getType() +  answeredQuestions.get(0).getDifficulty() +  answeredQuestions.get(0).getCategory());


       statisticsAnalyser = new StatisticsAnalyser(player);
       statisticsAnalyser.afterRound(answeredQuestions);

    }





    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.goToHomeBtn:
                // Intent
                goToHome = new Intent(this, MainMenuActivity.class);
                goToHome.putExtra(Constants.playerConstant, player);
                //goToHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
               // startActivityIfNeeded(goToHome, 0);
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