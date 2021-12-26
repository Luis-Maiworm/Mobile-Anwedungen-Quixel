package com.example.quizz.data.playerData;

import java.io.Serializable;

/**
 * This class sets the attributes needed for a {@code Player}. This includes also the {@link Statistics}.
 * Every {@code Player} has an own {@code Statistics} Object.
 * The {@code Player}'s attributes will be stored in a json file by the {@link PlayerManager}.
 */
public class Player implements Serializable {

    public Player(String playerName, int playerID){
        this.playerName = playerName;
        this.playerID = playerID;                    //todo playerID
    }


    //todo create empty constructor? just for one time reference use in loading the app for the first time

    public Player(){

    }


    public String playerName;
    public int playerID = 0;
    public Statistics stats;
    public int playerIcon;

    /*
    private Settings settings;

    public Settings getSettings(){
        return this.settings;
    }
    public void setSettings(Settings settings){
        this.settings = settings;
    }

    */

    public int getPlayerIcon() {
        return playerIcon;
    }
    public void setPlayerIcon(int playerIcon) {
        this.playerIcon = playerIcon;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerID() {
        return playerID;
    }
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Statistics getStats() {
        return stats;
    }
    public void setStats(Statistics stats) {
        this.stats = stats;
    }


}
