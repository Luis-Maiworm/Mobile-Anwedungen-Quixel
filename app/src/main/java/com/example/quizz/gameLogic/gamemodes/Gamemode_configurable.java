package com.example.quizz.gameLogic.gamemodes;

import android.os.Bundle;

import com.example.quizz.R;
import com.example.quizz.view.GamemodeActivity;


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
    @Override
    public void begin() {
        super.begin();
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


}