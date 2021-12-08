package com.example.quizz.data.playerData;

import com.example.quizz.gameLogic.PlayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Profiles} class includes the {@code playerList} with every {@link Player} created.
 * Every operation which needs to operate on the {@code playerList} will be declared in this
 * class. Only one original {@link Profile} instance should exist in the {@link PlayerManager}
 */




public class Profile {


    /**
     * Includes every {@link Player}.
     */
    public List<Player> playerList = new ArrayList<Player>();

    /**
     * Getter method for the {@code playerList}
     * @return {@code List<Player> playerList}
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Setter method for the {@code playerList}
     * @param playerList Will be set into the playerList
     */
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }



    /**
     * Method replaces a player with given ID with a new Player.
     * @param newPlayer Is the object which is replacing the old one.
     * @param oldPlayerId Is the ID of the old player, which will be replaced.
     */
    public void replacePlayerWithId(Player newPlayer, int oldPlayerId){
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i).getPlayerID() == oldPlayerId){
                playerList.set(i, newPlayer);
            }
        }
    }

    /**
     * Returns the player with the give ID.
     * @param id Key value, which will give the right {@link Player}
     * @return Returns the requested {@code Player}
     */
    public Player getPlayerWithId(int id) {
        for (Player player : playerList) {
            if (player.getPlayerID() == id) {
                return player;
            }
        }
        return null;
    }


    /**
     * Simply adds a player to the {@code playerList}
     * @param player The {@link Player} Object which will be added to the list.
     */
    public void addPlayer(Player player){
        this.playerList.add(player);
    }


    /**
     * Checks if a {@link Player} name already exists or not.
     * @param name String which will be checked.
     * @return True if it exists, false if not.
     */
    public boolean nameExists(String name){
        for (int i = 0; i < this.playerList.size(); i++){
            if(name.equals(this.playerList.get(i).getPlayerName())){
                System.out.println(playerList.get(i).getPlayerName());
                return true;
            }
        }
        return false;
    }


    /**
     * @return The size of the {@code playerList}
     */
    public int getPlayerListSize(){
        return this.playerList.size();
    }

    /**
     * Removes a {@link Player} from the {@code playerList}.
     * Iterates through to check if the {@code player} exists already. Use {@link #removePlayerFromList(int playerID)} if you have the Player ID.
     * @param player Will be removed.
     */
    public void removePlayerFromList(Player player){
        for (int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).equals(player)){
                playerList.remove(playerList.get(i));
            }
        }
    }

    /**
     * Removes a {@link Player} from the {@code playerList}.
     * Iterates through to check if the {@code id} exists already. Use {@link #removePlayerFromList(Player player)} if you have a Player Object.
     * @param id Corresponding {@code Player} Will be removed.
     */
    public void removePlayerFromList(int id){
        playerList.removeIf(player -> player.getPlayerID() == id);
    }



}
