package com.example.quizz.gameLogic.gamemodes;

import android.os.Bundle;

import com.example.quizz.R;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;
import com.example.quizz.view.GamemodeActivity;

import java.util.Locale;
import java.util.Objects;


public class Gamemode_configurable extends GamemodeActivity implements IGameSettings {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode);

        setup();
        begin();

    }

    @Override
    public void setup() {
        super.setup();
    }

    public void begin() {
        // Gets the data
        String difficulty = getIntent().getStringExtra("difficulty");
        System.out.println("DIFFI" + difficulty);
        String type = getIntent().getStringExtra("type");
        System.out.println("TYPE" + type);
        int value = getIntent().getIntExtra("value", 10);
        System.out.println("VALUE: " + value);
        // Starts the custom game
        super.begin(getTypeEnumByString(type), Objects.requireNonNull(getDifficultyEnumByString(difficulty)).toUpperCase(), value);
    }
    public String description() {
        return super.description("Completely Customizable Gamemode");

    }
    @Override
    public String getGamemodeName() {
        return this.getClass().getSimpleName();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * Finds the relevant ENUM by the string transferred by the intent
     * @param string String delivered by the intent
     * @return normal name from the ENUM which is needed to make the API call
     */
    private String getTypeEnumByString(String string) {
        for(Types t : Types.values() ) {
            if((string.equals(t.getRealName()))) {
                return t.getName();
            }
            else if(string.equalsIgnoreCase("Random")) {
                return "";
            }
        }
        return null;
    }
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


}