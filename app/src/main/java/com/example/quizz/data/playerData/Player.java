package com.example.quizz.data.playerData;

import java.io.Serializable;

public class Player implements Serializable {

    private int gamesWon = 2;


    public Player(int won) {
        this.gamesWon = won;
    }



    public int getGamesWon() {
        return this.gamesWon;
    }



}
