package com.example.quizz.gameLogic.gamemodes;

/**
 * Interface for every gamemode. Allows to
 * customize the setup and begin methode for
 * every gamemode without having lots of code
 * duplicates
 */
public interface IGameSettings {
    /**
     * Configures the Game (UI) Elements based
     * on the selected Game Mode
     */
    public void setup();
    /**
     * Gives a signal to the game logic to
     * start the game and makes the first
     * API call / receives relevant information
     */
    public void begin();

    /**
     * Returns the description of the active
     * Game Mode
     * @return description
     */
    public String description(String desc);

    /**
     * Returns the gamemode Name
     * @return gamemode name
     */
    public String getGamemodeName();
}
