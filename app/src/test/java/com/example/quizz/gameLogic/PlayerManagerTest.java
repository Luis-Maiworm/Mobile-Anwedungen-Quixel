package com.example.quizz.gameLogic;

import static org.junit.Assert.*;
import com.example.quizz.data.playerData.Player;
import org.junit.Before;
import org.junit.Test;

public class PlayerManagerTest {

    PlayerManager pManager;


    @Before
    public void init(){
        pManager = new PlayerManager();
    }

    /**
     * Check if a name containing only one character does throw an Exception, creating the Player.
     * And check if the message is as expected.
     */
    @Test
    public void createPlayerNameTooShort(){
        Player player = new Player();
        player.setPlayerName("A");

        Exception thrown = assertThrows(
                Exception.class,
                () -> pManager.createNewPlayer(player));

        assertEquals("Name needs to have at least 2, and a maximum of 10 letters", thrown.getMessage());
    }

    /**
     * Check if a name containing 11 characters does throw a Exception, creating the Player.
     * Check if message is as expected.
     */
    @Test
    public void createPlayerNameTooLong(){
        Player player = new Player();
        player.setPlayerName("ABCDEFGHIJK");

        Exception thrown = assertThrows(
                Exception.class,
                () -> pManager.createNewPlayer(player));

        assertEquals("Name needs to have at least 2, and a maximum of 10 letters", thrown.getMessage());
    }

    /**
     * Check the last Exception cause: Duplicate player name.
     * One player gets created with a correct and valid name.
     * Then another gets created, with the same name, causing an exception.
     */
    @Test
    public void createPlayerDuplicate(){
        Player playerCorrect = new Player();
        playerCorrect.setPlayerName("Test");


        try {
            pManager.createNewPlayer(playerCorrect);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Player playerIncorrect = new Player();
        playerIncorrect.setPlayerName("Test");

        Exception thrown = assertThrows(
                Exception.class,
                () -> pManager.createNewPlayer(playerIncorrect));


        assertEquals(1, pManager.profiles.getPlayerListSize());
        assertEquals("Name already used. Please try again", thrown.getMessage());
    }

    /**
     * Creates a player, and chooses this player as the "currentPlayer".
     * Then check, if the currentPlayer is still the same instance as the one created initially.
     */
    @Test
    public void chooseCurrentPlayer(){
        Player player = new Player();
        player.setPlayerName("Test");
        try {
            pManager.createNewPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pManager.chooseCurrentPlayer("Test");

        assertEquals(player, pManager.getCurrentPlayer());
    }

    /**
     * Create two Players (so the Exception, checking if there is just one Player, does not get thrown before this one)
     * make one Player to the currentPlayer and try to delete it.
     * This causes the exception.
     */
    @Test
    public void deletePlayerCurrentPlayer(){
        Player player = new Player();
        player.setPlayerName("Test");

        Player playerTwo = new Player();
        playerTwo.setPlayerName("TestTwo");

        try {
            pManager.createNewPlayer(player);
            pManager.createNewPlayer(playerTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pManager.chooseCurrentPlayer("Test");

        Exception thrown = assertThrows(
                Exception.class,
                () -> pManager.deletePlayer("Test"));

        assertEquals("Chosen profile is currently logged in. Change Profile to delete it.", thrown.getMessage());
    }

    /**
     * If theres only one Player in the list, an Exception gets thrown.
     */
    @Test
    public void deletePlayerOnlyOneExisting(){
        Player player = new Player();
        player.setPlayerName("Test");

        try {
            pManager.createNewPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Exception thrown = assertThrows(
                Exception.class,
                () -> pManager.deletePlayer("Test"));


        assertEquals("One Profile needs to exist.", thrown.getMessage());
    }

    @Test
    public void deletePlayerCorrectly(){
        Player player = new Player();
        player.setPlayerName("Test");

        Player playerTwo = new Player();
        playerTwo.setPlayerName("TestTwo");

        try {
            pManager.createNewPlayer(player);
            pManager.createNewPlayer(playerTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pManager.chooseCurrentPlayer("TestTwo");

        assertEquals(2, pManager.getProfiles().getPlayerListSize());

        try {
            pManager.deletePlayer("Test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, pManager.getProfiles().getPlayerListSize());
    }

}