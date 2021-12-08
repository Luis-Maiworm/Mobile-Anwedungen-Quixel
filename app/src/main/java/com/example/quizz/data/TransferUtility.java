package com.example.quizz.data;

import com.example.quizz.data.playerData.Player;

/**
 * Simple Class to transfer Strings and Player instances between classes and activities where
 * intents are not possible
 */
public class TransferUtility {

    private String savedString;
    private Player savedPlayer;


    public TransferUtility() {
    }

    public String getSavedString() {
        return savedString;
    }

    public void setSavedString(String savedString) {
        this.savedString = savedString;
        System.out.println(savedString);
    }

    public Player getSavedPlayer() {
        return savedPlayer;
    }

    public void setSavedPlayer(Player savedPlayer) {
        this.savedPlayer = savedPlayer;
    }
}



