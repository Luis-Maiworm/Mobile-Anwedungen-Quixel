package com.example.quizz.gameLogic.gamemodes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.quizz.R;
import com.example.quizz.view.GamemodeActivity;

/**
 * WORK IN PROGRESS |
 */
public class Gamemode_endless extends GamemodeActivity implements IGameSettings, IGamemode{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode);

        setup();


    }

    @Override
    public void setup() {
        super.setup();
        System.out.println("ENDLESS MODE");
    }


    public void begin() {
        super.begin("","", 10);

    }

    @Override
    public String description(String desc) {
        return null;
    }

    @Override
    public String getGamemodeName() {
        return null;
    }
}