package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;
import com.example.quizz.exceptions.QueryException;
import com.example.quizz.gameLogic.gamemodes.Gamemode_configurable;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Custom Game Mode Activity where the player can configure all relevant game settings to
 * generate a custom API call
 */
public class ConfigureGamemodeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // Spinner Menus
    private Spinner difficultySpinner, typeSpinner;
    // View
    private TextView titleLabel, selectedCategory, selectDifficulty, selectType;
    private Button startBtn;
    // Icon from selected category
    private ImageView icon;
    // Spinner Menu Items
    private final String[] difficulties = {Difficulties.EASY.getName(), Difficulties.MEDIUM.getName(), Difficulties.HARD.getName(), "Random"};
    private final String[] questionTypes = {Types.TRUEFALSE.getRealName(), Types.MULTIPLECHOICE.getRealName(), "Random"};
    // Spinner Array Adapter
    private ArrayAdapter<String> difficultyAdapter, typeAdapter;
    private String difficulty, type;
    private QuestionManager questionManager;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gamemode);

        setup();

        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //********************************************************************************//
        //                                 IGNORES THREAD ERRORS                          //
        //********************************************************************************//

    }

    /**
     * Sets up the View / creates the spinner and the Spinner Adapters
     */
    private void setup() {
        // View
        titleLabel = findViewById(R.id.configureTitleLabel);
        selectDifficulty = findViewById(R.id.selectDifficultyLabel);
        selectType = findViewById(R.id.selectQuestionTypeLabel);
        selectedCategory = findViewById(R.id.categoryConfigLabel);
        selectedCategory.setText(getResources().getString(R.string.selected_category_config, getIntent().getStringExtra("category")));
        startBtn = findViewById(R.id.configStartBtn);
        startBtn.setOnClickListener(this);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        typeSpinner = findViewById(R.id.qTypeSpinner);
        icon = findViewById(R.id.categoryIconConfig);
        icon.setImageResource(getIntent().getIntExtra("categoryIcon", -1));
        // Spinner Menu and Adapter Setup
        difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        difficultySpinner.setOnItemSelectedListener(this);
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);
        // Init QuestionManager
        questionManager = new QuestionManager();
    }


    // Spinner Listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String debug = parent.getItemAtPosition(position).toString();

    }
    // Spinner Listener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // Button Listener
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.configStartBtn) {
            difficulty = difficultySpinner.getSelectedItem().toString();
            type = typeSpinner.getSelectedItem().toString();
            confirm();
        }
    }

    /**
     * User can confirm the selected settings to start the game or reselect them
     */
    private void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Game");
        builder.setMessage("Start a new game with " + difficulty + " difficulty and " + type + " question types?");
        builder.setPositiveButton(R.string.quitYes, (dialog, id) -> {
            try {
                value = checkQuestionValue();
                System.out.println("VALUE: " + value);
            } catch (IOException | QueryException e) {
                e.printStackTrace();
            }
                if(value > 0 && value < 10) {
                    startGame(value);
                }
                else if(value == 0) {
                    String message = "Error: There are no available Questions based on your configuration. Please consider changing the values!";
                    new AlertDialog.Builder(ConfigureGamemodeActivity.this)
                            .setTitle("Invalid Configuration")
                            .setMessage(message)
                            .setPositiveButton("ok", null)
                            .show();
                }
                else if(value > 10) {
                    startGame(10);
                }

        });
        builder.setNegativeButton(R.string.QuitCancel, (dialog, id) -> {
        });
        builder.show();
    }

    /**
     * Checks how many questions are available based on the users configuration before starting the
     * game to ensure, that there are enough questions
     * @return number of questions available (max 50)
     * @throws IOException when the QuestionData is not in a valid JSON format
     * @throws QueryException when the API-Call Query is not valid / API is not responding
     */
    private int checkQuestionValue() throws IOException, QueryException {
        List<Question> questionList;
        System.out.println(Categories.valueOf(getIntent().getStringExtra("category").toUpperCase()).getId());
        System.out.println(getDifficultyEnumByString(difficulty).toUpperCase());
        System.out.println(getTypeEnumByString(type).toUpperCase());
        questionList = questionManager.getApiData(50, Categories.valueOf(getIntent().getStringExtra("category").toUpperCase()).getId(), getDifficultyEnumByString(difficulty).toLowerCase(), getTypeEnumByString(type).toLowerCase());
        return questionList.size();
    }

    /**
     * Starts the Game loop
     * @param value number of questions
     */
    private void startGame(int value) {
        Intent startGame = new Intent(ConfigureGamemodeActivity.this, Gamemode_configurable.class);
        startGame.putExtra("difficulty", difficulty);
        startGame.putExtra("type", type);
        startGame.putExtra("category", getIntent().getStringExtra("category"));
        startGame.putExtra("value", value);
        startGame.putExtra(Constants.playerConstant, getIntent().getSerializableExtra(Constants.playerConstant));
        startActivity(startGame);
    }

    /**
     * Compares the Option selected in the spinner to find the fitting ENUM for the API call
     * @param string name of the type string
     * @return name of the ENUM / "" for the random option / null if something went wrong
     */
    private String getTypeEnumByString(String string) {
        for(Types t : Types.values() ) {
            if((string.equalsIgnoreCase(t.getRealName()))) {
                return t.getName();
            }
            else if(string.equalsIgnoreCase("Random")) {
                return "";
            }
        }
        return null;
    }
    /**
     * Compares the Option selected in the spinner to find the fitting ENUM for the API call
     * @param string name of the difficulty string
     * @return name of the ENUM / "" for the random option / null if something went wrong
     */
    private String getDifficultyEnumByString(String string) {
        for(Difficulties d : Difficulties.values() ) {
            if((string.equalsIgnoreCase(d.getName()))) {
                return d.getName();
            }
            else if(string.equalsIgnoreCase("Random")) {
                return "";
            }
        }
        return null;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Configuration");
        builder.setMessage("Do you really want to go back?");
        builder.setPositiveButton(R.string.quitYes, (dialog, id) -> {
            finish();
        });
        builder.setNegativeButton(R.string.QuitCancel, (dialog, id) -> {
        });
        builder.show();
    }

}