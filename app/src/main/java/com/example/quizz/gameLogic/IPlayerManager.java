package com.example.quizz.gameLogic;

import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.Profile;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * The PlayerManager includes methods and operations acting
 * as the main interface between the GUI and the GameManager.
 * It contains the original {@link Profile} instance.
 * It can load and save the state of the Profile. It can set
 * the currentPlayer which will be the {@link Player} to
 * operate on during the whole game process.
 */
public interface IPlayerManager {

    /**
     * Method sets the {@code currentPlayer} with a given ID.
     * This method checks also, if a profile is in use already,
     * or if the profile gets chosen for the first time in
     * this session.
     * @param newId The ID will be the chosen {@link Player}.
     */
    public void chooseCurrentPlayer(int newId);

    /**
     * Method saves the state of the {@code currentPlayer}
     * into its original position, to the initial entry in
     * the {@code playerList}.
     * <p>If the Player changes -> It should first save
     * the Player already in use, using this method, and
     * then it can use the {@link #chooseCurrentPlayer(int)}
     * method, to choose the "new currentPlayer": </p>
     * @param id playerID
     * @param currentPlayer player Instance
     */
    public void saveCurrentPlayer(Player currentPlayer, int id);

    public Profile getProfiles();

    /**
     * Declares and initializes a new Player. Also checks
     * if the given name already exists.
     * @param playerName name of the {@link Player} to create.
     */
    public void createNewPlayer(String playerName) throws IllegalArgumentException;

    /**
     * Renames a player.
     * @param id Is the {@code Player} which will be renamed.
     * @param newPlayerName Is the new name for the player.
     */
    public void renamePlayer(int id, String newPlayerName);

    /**
     * Persistently saves all Player Profiles
     * @param profiles player profiles
     * @throws IOException when saving process failed
     */
    public void saveToJson(Profile profiles) throws IOException;

    /**
     * Loads all Player Profiles
     * @throws FileNotFoundException when file doesn't exist
     */
    public void loadFromJson() throws FileNotFoundException;
}
