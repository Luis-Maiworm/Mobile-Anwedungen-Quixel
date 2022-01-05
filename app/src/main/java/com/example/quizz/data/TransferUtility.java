package com.example.quizz.data;

import com.example.quizz.data.playerData.Player;
import com.example.quizz.gameLogic.PlayerManager;

import java.io.Serializable;

/**
 * Simple Class to transfer Strings and Player instances between classes and activities where
 * intents are not possible
 */
public class TransferUtility implements Serializable {

    private String savedString;
    private Player savedPlayer;
    private PlayerManager playerManager;


    public TransferUtility() {
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
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



