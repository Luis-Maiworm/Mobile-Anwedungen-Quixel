package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.R;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;

/**
 * Custom Game Mode Activity where the player can configure all relevant game settings to
 * generate a custom API call
 */
public class ConfigureGamemodeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gamemode);

        // View
        titleLabel = findViewById(R.id.configureTitleLabel);
        selectDifficulty = findViewById(R.id.selectDifficultyLabel);
        selectType = findViewById(R.id.selectQuestionTypeLabel);
        selectedCategory = findViewById(R.id.categoryConfigLabel);
        selectedCategory.setText(getResources().getString(R.string.selected_category_config, getIntent().getStringExtra("category")));
        startBtn = findViewById(R.id.configStartBtn);
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



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String debug = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), debug, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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