package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Types;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.exceptions.QueryException;
import com.example.quizz.gameLogic.gamemodes.IGameSettings;
import com.example.quizz.gameLogic.gamemodes.IGamemode;
import com.example.quizz.network.Wrapper;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * GamemodeActivity master class. Contains implementations of all relevant
 * methods and GUI Elements used in a game loop by every game mode.
 */
public class GamemodeActivity extends AppCompatActivity implements IGameSettings, IGamemode, View.OnClickListener {

    // View Setup
    private Button answerBtn1, answerBtn2, answerBtn3, answerBtn4;
    private TextView questionTextLabel, categoryLabel, pointCntLabel, correctIncorrectLabel, questionNumberLabel, correctQLabel, incorrectQLabel;
    private ProgressBar timerBar;
    // Q-Manager Setup
    private QuestionManager qManager;
    // Timer Setup
    private CountDownTimer gameTimer;
    // Data Setup
    private String categoryIdentifier, questionType, questionDifficulty, FLAG;
    private Question activeQuestion;
    private ArrayList<Question> activeQuestions;
    private ArrayList<String> activeAnswers;
    private int points, correct, incorrect, questionCounter = 0;
    private int time, questionValue;
    // Player
    private Player currentPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode);
    }

    /**
     * First master and setup method that configures all relevant UI items, buttons, managers and
     * the active player instance
     */
    @Override
    public void setup() {
        // Setup View
        categoryLabel = findViewById(R.id.categoryGameTitle);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);
        pointCntLabel = findViewById(R.id.pointsLabel);
        correctIncorrectLabel = findViewById(R.id.correctOrIncorrect);
        questionNumberLabel = findViewById(R.id.questionCounterLabel);
        correctQLabel = findViewById(R.id.correctQuestionsLabel);
        incorrectQLabel = findViewById(R.id.incorrectQuestionsLabel);
        timerBar = findViewById(R.id.gameTimerProgressbar);
        questionTextLabel = findViewById(R.id.questionTextView);
        // Init the Score
        pointCntLabel.setText(getString(R.string.scoreLabel, points));
        correctQLabel.setText(getString(R.string.correctQuestionCounterLabel, correct));
        incorrectQLabel.setText(getString(R.string.incorrectQuestionCounterLabel, incorrect));
        // Listener for the buttons
        answerBtn1.setOnClickListener(this);
        answerBtn2.setOnClickListener(this);
        answerBtn3.setOnClickListener(this);
        answerBtn4.setOnClickListener(this);

        // Creates the Question Manager
        qManager = new QuestionManager();
        // Gets the active PlayerManager instance
        if (getIntent().hasExtra(Constants.playerConstant)) {
            currentPlayer = (Player) getIntent().getSerializableExtra(Constants.playerConstant);
        }
        else if(getIntent().hasExtra("flag")) {
            Wrapper wrap = (Wrapper) getIntent().getSerializableExtra("flag");
            currentPlayer = wrap.getPlayer();
        }


    }

    /**
     * Second master method that starts the game loop after the setup is finished
     */
    @Override
    public void begin(String type, String difficulty, int value) {
        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//

        // sets the FLAG (SP = Singleplayer | MP = Multiplayer)
        FLAG = "sp";
        // Receives Data and calls the API
        receiveCategory(type, "", value);
        receiveQuestion();
        // Init the Category Label
        categoryLabel.setText(Categories.valueOf(categoryIdentifier).getName());
    }

    /**
     * Alternative Begin Method for the Multiplayer Mode
     * @param questionList already generated questions
     */
    public void begin(ArrayList<Question> questionList) {
        FLAG = "mp";
        activeQuestions = questionList;
        receiveQuestion();
    }


    /**
     * Listener for all 4 answers-buttons
     * @param v current view
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answerBtn1:
                if (activeQuestion.getCorrect_answer().equalsIgnoreCase(answerBtn1.getText().toString())) {
                    activeQuestion.setCorrect(true);
                    updateGUI("true");
                } else {
                    activeQuestion.setCorrect(false);
                    updateGUI("false");
                }
                break;
            case R.id.answerBtn2:
                if (activeQuestion.getCorrect_answer().equalsIgnoreCase(answerBtn2.getText().toString())) {
                    activeQuestion.setCorrect(true);
                    updateGUI("true");
                } else {
                    activeQuestion.setCorrect(false);
                    updateGUI("false");
                }
                break;
            case R.id.answerBtn3:
                if (activeQuestion.getCorrect_answer().equalsIgnoreCase(answerBtn3.getText().toString())) {
                    activeQuestion.setCorrect(true);
                    updateGUI("true");
                } else {
                    activeQuestion.setCorrect(false);
                    updateGUI("false");
                }
                break;
            case R.id.answerBtn4:
                if (activeQuestion.getCorrect_answer().equalsIgnoreCase(answerBtn4.getText().toString())) {
                    activeQuestion.setCorrect(true);
                    updateGUI("true");
                } else {
                    activeQuestion.setCorrect(false);
                    updateGUI("false");
                }
                break;
            default:
                break;
        }
        receiveQuestion();
        updateButtons();
    }

    /**
     * Updates to GUI based on the answer
     *
     * @param option true / false / question / time for specific GUI reaction
     */
    @SuppressLint("SetTextI18n")
    public void updateGUI(String option) {
        switch (option) {
            case "true":
                points = points + 1;
                correct = correct + 1;
                correctIncorrectLabel.setTextColor(Color.GREEN);
                correctIncorrectLabel.setText("CORRECT!");
                correctQLabel.setText(getString(R.string.correctQuestionCounterLabel, correct));
                pointCntLabel.setText(getString(R.string.scoreLabel, points));
                gameTimer.cancel();
                break;
            case "false":
                points = points - 1;
                incorrect = incorrect + 1;
                correctIncorrectLabel.setTextColor(Color.RED);
                correctIncorrectLabel.setText("INCORRECT!");
                incorrectQLabel.setText(getString(R.string.incorrectQuestionCounterLabel, incorrect));
                pointCntLabel.setText(getString(R.string.scoreLabel, points));
                gameTimer.cancel();
                break;
            case "question":
                questionTextLabel.setText(activeQuestion.getQuestion());
                questionNumberLabel.setText(getString(R.string.question_x_out_of_y_label, questionCounter, activeQuestions.size()));
                // Identify random category in multiplayer mode, since there is no category selection
                if (categoryIdentifier == null) {
                    categoryLabel.setText(activeQuestion.getCategoryString().toUpperCase());
                }
                scatterAnswers();
                startTimer();
                break;
            case "time":
                points = points - 1;
                incorrect = incorrect - 1;
                correctIncorrectLabel.setTextColor(Color.RED);
                correctIncorrectLabel.setText("TIME IS OVER");
                pointCntLabel.setText(getString(R.string.scoreLabel, points));
                incorrectQLabel.setText(getString(R.string.incorrectQuestionCounterLabel, incorrect));
                gameTimer.cancel();
                receiveQuestion();
                updateButtons();
                break;
            default:
                System.err.println("Something went wrong :/");
                break;
        }
    }


    /**
     * Updates the GUI based on the type of active question
     */
    public void updateButtons() {
        if (questionType.equalsIgnoreCase(Types.TRUEFALSE.getName())) {
            answerBtn3.setVisibility(View.GONE);
            answerBtn4.setVisibility(View.GONE);
        } else if (questionType.equalsIgnoreCase(Types.MULTIPLECHOICE.getName())) {
            answerBtn3.setVisibility(View.VISIBLE);
            answerBtn4.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Fills every button with the possible answers for the active question.
     * Adds TRUE and FALSE if the active question is an True / False Question.
     * Adds all 4 possible answers if the question is a multiple choice question.
     */
    private void scatterAnswers() {
        if (questionType.equalsIgnoreCase(Types.MULTIPLECHOICE.getName())) {
            activeAnswers = activeQuestion.getAllAnswers();
            randomOrder();
        }
        else if (questionType.equalsIgnoreCase(Types.TRUEFALSE.getName())) {
            activeAnswers = new ArrayList<>(Arrays.asList("TRUE", "FALSE"));
            answerBtn1.setText(activeAnswers.get(0));
            answerBtn2.setText(activeAnswers.get(1));
        }
        else {
            Toast.makeText(this, "An Error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A simple method the shuffle the answers so that the player cant predict the correct
     * answer based on its position
     */
    private void randomOrder() {

        Random random = new Random();
        int order = random.nextInt((4));
        switch (order) {
            case 0:
                answerBtn1.setText(activeAnswers.get(0));
                answerBtn2.setText(activeAnswers.get(1));
                answerBtn3.setText(activeAnswers.get(2));
                answerBtn4.setText(activeAnswers.get(3));
                break;
            case 1:
                answerBtn1.setText(activeAnswers.get(2));
                answerBtn2.setText(activeAnswers.get(0));
                answerBtn3.setText(activeAnswers.get(3));
                answerBtn4.setText(activeAnswers.get(1));
                break;
            case 2:
                answerBtn1.setText(activeAnswers.get(3));
                answerBtn2.setText(activeAnswers.get(2));
                answerBtn3.setText(activeAnswers.get(1));
                answerBtn4.setText(activeAnswers.get(0));
                break;
            case 3:
                answerBtn1.setText(activeAnswers.get(2));
                answerBtn2.setText(activeAnswers.get(1));
                answerBtn3.setText(activeAnswers.get(0));
                answerBtn4.setText(activeAnswers.get(3));
                break;
        }
    }

    /**
     * Receives the Category String from the Recycler View and saves it to a global variable
     * In case there is no Text, it return the String "No Date Found"
     */
    public void receiveCategory(String type, String difficulty, int value) {
        if (getIntent().hasExtra("category")) {
            categoryIdentifier = getIntent().getStringExtra("category").toUpperCase();
            questionType = type;
            questionValue = value;
            questionDifficulty = difficulty;
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interprets the incoming intent String and returns the category ID from the API
     *
     * @return 9-32 Category ID or -2 as an error code
     */
    public int interpretIncomingData() {
        if (!this.categoryIdentifier.equals("No Data Found")) {
            return Categories.valueOf(categoryIdentifier).getId();
        }
        return -2;
    }

    /**
     * Makes the API Call for the specific Category and saves the Question(s)
     *
     * @param ID category ID
     * @throws IOException    when there is something wrong with the API call
     * @throws QueryException if input is invalid (e.g. questionNumber=0)
     * @see QuestionManager#getApiData(int, int, String, String)
     */
    public void callAPI(int ID, int value, String type, String difficulty) throws IOException, QueryException {

        activeQuestions = (ArrayList<Question>) qManager.getApiData(value, ID, difficulty, type);
        updateButtons();
    }

    /**
     * Tries to receive the Questions generated by the API call based on the incoming intent String
     * Fills the list with a new set of questions and displays a new questions from the list after
     * the active questions was answered.
     * If the max questions limit is reached, the method will call the endGame() method
     *
     * @see #updateGUI(String)
     * @see #endGame()
     */
    public void receiveQuestion() {

        // Only calls the API if there are no stored questions
        if (activeQuestions == null)
            try {
                callAPI(interpretIncomingData(), questionValue, questionType, questionDifficulty);
            } catch (IOException | QueryException e) {
                e.printStackTrace();
            }
        // Displays the new questions until the max question limit is reached
        if (questionCounter < activeQuestions.size()) {
            activeQuestion = activeQuestions.get(questionCounter);
            questionCounter++;
            questionType = activeQuestion.getTypeString();
            updateButtons();
            updateGUI("question");
        }
        // If the limit is reached, the end screen will appear
        else {
            endGame();
        }
    }


    /**
     * Starts the end screen activity and passes all relevant data
     */
    public void endGame() {
        // After Singleplayer Round
        if (FLAG.equalsIgnoreCase("sp")) {
            Intent toEndScreen = new Intent(this, EndscreenActivity.class);
            toEndScreen.putExtra("score", points);
            toEndScreen.putExtra(Constants.playerConstant, currentPlayer);
            toEndScreen.putExtra("correct", correct);
            toEndScreen.putExtra("incorrect", incorrect);
            toEndScreen.putExtra("q", activeQuestions);
            finish();
            startActivity(toEndScreen);
        }
        // After Multiplayer Round
        if (FLAG.equalsIgnoreCase("mp")) {
            Intent toEndScreenMP = new Intent(this, EndscreenActivity.class);
            toEndScreenMP.putExtra("correct", correct);
            toEndScreenMP.putExtra("incorrect", incorrect);
            toEndScreenMP.putExtra("score", points);
            toEndScreenMP.putExtra(Constants.playerConstant, currentPlayer);
            toEndScreenMP.putExtra("q", activeQuestions);
            finish();
            startActivity(toEndScreenMP);
        }
    }

    /**
     * Starts and creates a new Countdown Timer.
     */
    public void startTimer() {
        time = 0;
        timerBar.setProgress(time);
        gameTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateProgressbar();
            }

            @Override
            public void onFinish() {
                updateGUI("time");
                time = 0;
                timerBar.setProgress(time);


            }
        }.start();
    }

    /**
     * Updates the Progress Bar UI element every tick
     *
     * @see #startTimer()
     * @see CountDownTimer#onTick(long)
     */
    private void updateProgressbar() {
        time = time + 5;
        timerBar.setProgress(time);
    }


    /**
     * Method that provides the gamemode description
     *
     * @param desc gamemode description
     * @return gamemode description
     */
    @Override
    public String description(String desc) {
        return "Mode Description: " + desc;
    }

    @Override
    public String getGamemodeName() {
        return this.getClass().getSimpleName();
    }

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    /**
     * Opens a dialog when the User presses the back key while
     * the game is running
     */
    @Override
    public void onBackPressed() {
        // Dialog Setup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Game");
        builder.setMessage("Do you really want to quit? Progress won't be saved!");
        builder.setPositiveButton(R.string.quitYes, (dialog, id) -> finish());
        builder.setNegativeButton(R.string.QuitCancel, (dialog, id) -> {
        });
        builder.show();
    }
}


