package com.example.quizz.gameLogic.gamemodes;

import android.os.Bundle;

import com.example.quizz.R;
import com.example.quizz.view.GamemodeActivity;


/**
 * First simple standard game mode. No user configuration, just random questions
 * from a specific category chosen by the user. Ends after the max Numer of
 * Questions is reached (10)
 *
 */
public class Gamemode_standard extends GamemodeActivity implements IGameSettings, IGamemode  {
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
        super.begin("", "", 10);
    }


    public String description() {
        return super.description("First simple standard game mode");

    }

    @Override
    public String getGamemodeName() {
       return this.getClass().getSimpleName();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
