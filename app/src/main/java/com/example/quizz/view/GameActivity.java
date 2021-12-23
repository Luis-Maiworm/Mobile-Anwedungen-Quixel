package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.quizz.R;
import com.example.quizz.data.enums.Categories;
import com.example.quizz.data.enums.Constants;
import com.example.quizz.exceptions.QueryException;
import com.example.quizz.gameLogic.timerLogic.Timer;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;
import java.io.IOException;



public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    // View
    private Button trueBtn, falseBtn, newQ;
    private Question activeQuestion;
    private TextView questionText, category, timer, pointCnt, correctIncorrectLabel;
    // Q-Manager Setup
    private QuestionManager qManager;
    // Timer
    private Timer time;
    // Data
    private String categoryIdentifier;
    private int points = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        qManager = new QuestionManager();

        //********************************************************************************//
        //                                 DEVMODE                                        //
        //********************************************************************************//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //********************************************************************************//
        //                                 DEVMODE                                        //
        //********************************************************************************//

        // Receives Data and calls the API
        receiveCategory();
        receiveQuestion();


        // Setup View
        category = findViewById(R.id.categoryGameTitle);
        category.setText(categoryIdentifier);
        trueBtn = findViewById(R.id.trueBtn);
        falseBtn = findViewById(R.id.falseBtn);
        pointCnt = findViewById(R.id.pointsLabel);
        pointCnt.setText("Score: " + points);
        correctIncorrectLabel = findViewById(R.id.correctOrIncorrect);
        // Listener
        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.trueBtn:
                if(activeQuestion.getCorrect_answer().equalsIgnoreCase("True")) {
                    points = points+1;
                    correctIncorrectLabel.setText("CORRECT!");
                    correctIncorrectLabel.setTextColor(Color.GREEN);
                }
                else {
                    points = points-1;
                    correctIncorrectLabel.setText("INCORRECT!");
                    correctIncorrectLabel.setTextColor(Color.RED);
                }
                pointCnt.setText("Score: " + points);;
                receiveQuestion();
                break;
            case R.id.falseBtn:
                if(!activeQuestion.getCorrect_answer().equalsIgnoreCase("True")) {
                    points = points+1;
                    correctIncorrectLabel.setTextColor(Color.GREEN);
                    correctIncorrectLabel.setText("CORRECT!");
                }
                else {
                    points = points-1;
                    correctIncorrectLabel.setTextColor(Color.RED);
                    correctIncorrectLabel.setText("INCORRECT!");
                }
                pointCnt.setText("Score: " + points);
                receiveQuestion();
                break;
            default:
                break;
        }
    }



    private void receiveCategory() {
        if(getIntent().hasExtra(Constants.categoryConstant)) {
            categoryIdentifier = getIntent().getStringExtra(Constants.categoryConstant);
        }
        else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private int interpretIncomingData() {
        if(!this.categoryIdentifier.equals("No Data Found")) {
            return Categories.valueOf(categoryIdentifier).getId();
        }
        return -1;
    }

    private void callAPI(int ID) throws IOException, QueryException {
        activeQuestion = qManager.getApiData(1, ID, "", "boolean").get(0);
    }
    private void receiveQuestion() {

        try {
            callAPI(interpretIncomingData());
        } catch (IOException | QueryException e) {
            e.printStackTrace();
        }
        questionText = findViewById(R.id.questionTextView);
        questionText.setText(activeQuestion.getQuestion());
    }
}