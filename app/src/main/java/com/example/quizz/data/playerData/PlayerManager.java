package com.example.quizz.data.playerData;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;


import java.io.*;
import java.util.List;

/**
 * The StatisticsManager includes methods and operations acting as the main interface between the GUI and the GameLogic.
 * It contains the original {@link Profile} instance. It can load and save the state of the Profile. It can set
 * the currentPlayer which will be the {@link Player} to operate on during the whole game process.
 */


//todo StatisticsManager ->

public class PlayerManager implements Parcelable {

    private final String DEFAULTPATH = "data/stats.json";
    File json = new File(DEFAULTPATH);
    Gson gson = new Gson();

    public Profile profiles = new Profile();
    List<Player> playerList;

    Player currentPlayer;

    private String stuff;   //todo -> parcelable konstruktor .. - was das

    public PlayerManager(){
        playerList = profiles.getPlayerList();
    }

    protected PlayerManager(Parcel in) {
        stuff = in.readString();
    }

    public static final Creator<PlayerManager> CREATOR = new Creator<PlayerManager>() {
        @Override
        public PlayerManager createFromParcel(Parcel in) {
            return new PlayerManager(in);
        }

        @Override
        public PlayerManager[] newArray(int size) {
            return new PlayerManager[size];
        }
    };

    /**
     * Method sets the {@code currentPlayer} with a given ID.
     * This method checks also, if a profile is in use already,
     * or if the profile gets chosen for the first time in this session.
     * @param newId The ID will be the chosen {@link Player}.
     */
    @Deprecated
    public void chooseCurrentPlayer(int newId){

        if (currentPlayer != null) {
            profiles.replacePlayerWithId(profiles.getPlayerWithId(newId), this.currentPlayer.getPlayerID());
        }

        this.currentPlayer = profiles.getPlayerWithId(newId);
    }

    /**
     * Sets the current Player.
     * @param playerName should deliver an existing name within the {@code playerList}.
     * The corresponding player will be set as the "currentPlayer".
     */
    public void chooseCurrentPlayer(String playerName) {
        currentPlayer = null;
        /*
        if (currentPlayer != null) {
            profiles.replacePlayerWithName(profiles.getPlayerWithName(playerName), this.currentPlayer.getPlayerName());
        }*/
        this.currentPlayer = profiles.getPlayerWithName(playerName);

        this.profiles.setCurrentPlayer(currentPlayer.getPlayerName());

    }

    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * Method saves the state of the {@code currentPlayer}, into its original position, to the
     * initial entry in the {@code playerList}. This method should be called, when the currentPlayer
     * needs to be changed or the program closes.
     * <p>If the Player changes -> It should first save the Player already in use, using this method, and then it can
     * use the {@link #chooseCurrentPlayer(int)} method, to choose the "new currentPlayer": </p>
     * Due to the fact, that the
     * {@code currentPlayer} and the original {@code Player} have the same ID it
     * is easy to put it in the right place again.
     * @param id put the {@code currentPlayer} and use the {@link Player#getPlayerID()} method to get the right ID.
     */
    public void saveCurrentPlayer(Player currentPlayer, int id){
        profiles.replacePlayerWithId(currentPlayer, id);
    }

    /**
     * Getter of the {@code currentPlayer}
     * @return {@code this.currentPlayer}
     */
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }



    public Profile getProfiles(){                  //todo notwendig oder nicht? -> wird profile benötigt um darauf operieren zu können oder macht das eh der sManager
        // -> falls unnötig -> saveToJson(Profiles profiles) parameter entfernen
        return this.profiles;
    }

    //playerOperationen

    /**
     * Declares and initializes a new Player. Also checks if the given name already exists.
     * @param playerName name of the {@link Player} to create.
     */
    public void createNewPlayer(String playerName) throws Exception {
        Player player;

        if (!this.profiles.nameExists(playerName)) {
            player = new Player(playerName, this.profiles.getPlayerListSize() + 1);           // ID ist immer die größe der SpielerList + 1
            player.setStats(new Statistics());
        } else {
            throw new Exception("Name already used");       //todo exception ändern
        }
        this.profiles.addPlayer(player);
    }

    /**
     *
     */
    public void createNewPlayer(Player player) throws Exception {
        if(!this.profiles.nameExists(player.getPlayerName())){
            player.setPlayerID(this.profiles.getPlayerListSize() + 1);
            player.setStats(new Statistics());
        } else {
            throw new Exception("Name already used. Please try again");
        }
        this.profiles.addPlayer(player);
    }


    /**
     * Renames a player.
     * @param id Is the {@code Player} which will be renamed.
     * @param newPlayerName Is the new name for the player.
     */
    public void renamePlayer(int id, String newPlayerName){
        this.profiles.getPlayerWithId(id).setPlayerName(newPlayerName);
    }






    public void loadFromJson(String jsonString) {
        if(jsonString=="") this.profiles = null;
        this.profiles = this.gson.fromJson(jsonString, Profile.class);

    }


    public String saveToJson() {
        return this.gson.toJson(profiles);
    }

    public void setProfiles(Profile profiles){
        this.profiles = profiles;
    }




    /*
    public void loadFromJson() throws FileNotFoundException {
        this.profiles = this.gson.fromJson(new FileReader(this.json), Profile.class);
    }

    @Deprecated
    public void saveToJson(Profile profiles) throws IOException {
         try(FileWriter writer = new FileWriter(this.json)){                     //todo this. ja nein?
            this.gson.toJson(profiles, writer);
         }
    }*/




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DEFAULTPATH);
    }
}
