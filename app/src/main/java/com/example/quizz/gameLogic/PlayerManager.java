package com.example.quizz.gameLogic;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.quizz.data.playerData.Player;
import com.google.gson.Gson;
import com.example.quizz.data.playerData.Profile;
import com.example.quizz.data.playerData.Statistics;
import java.util.ArrayList;

/**
 * The PlayerManager includes methods and operations acting as the main interface between the GUI and the GameLogic.
 * It contains the original {@link Profile} instance. It can load and save the state of the Profile. It can set
 * the currentPlayer which will be the {@link Player} to operate on during the whole game process.
 */

public class PlayerManager implements Parcelable {

    private final String DEFAULTPATH = "data/stats.json";
    private final Gson gson = new Gson();
    public Profile profiles = new Profile();
    private ArrayList<Player> playerList;
    private Player currentPlayer;

    public PlayerManager(){
        playerList = profiles.getPlayerList();
    }
    protected PlayerManager(Parcel in) {
        in.readString();
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
     * Sets the current Player.
     * @param playerName should deliver an existing name within the {@code playerList}.
     * The corresponding player will be set as the "currentPlayer".
     */
    public void chooseCurrentPlayer(String playerName) {
        currentPlayer = null;

        this.currentPlayer = profiles.getPlayerWithName(playerName);        //setzt den currentPlayer, damit das Player Objekt wiederverwertet werden kann
        this.profiles.setCurrentPlayer(currentPlayer.getPlayerName());      //setzt den String für den currentPlayer (name) -> später id, damit dieser in einer json gespeichert werden kann
    }

    /**
     * Sets currentPlayer from the SharedPreferences. (Profiles contains a String "currentPlayer" storing the name of the currentPlayer)
     *
     * @param currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * Method saves the state of the {@code currentPlayer}, into its original position, to the
     * initial entry in the {@code playerList}. This method should be called, when the currentPlayer
     * needs to be changed or the program closes.
     * <p>If the Player changes -> It should first save the Player already in use, using this method, and then it can
     * use the {@link #chooseCurrentPlayer(String)} method, to choose the "new currentPlayer": </p>
     * Due to the fact, that the
     * {@code currentPlayer} and the original {@code Player} have the same ID it
     * is easy to put it in the right place again.
     * @param name put the {@code currentPlayer} and use the {@link Player#getPlayerName()} ()} method to get the right name.
     */
    public void saveCurrentPlayer(Player currentPlayer, String name){
        profiles.replacePlayerWithName(currentPlayer, name);
    }

    /**
     * Getter of the {@code currentPlayer}
     * @return {@code this.currentPlayer}
     */
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }


    public Profile getProfiles(){
        return this.profiles;
    }


    /**
     * Creates a new Player and adds it to the profiles
     * @param player New Player
     */
    public void createNewPlayer(Player player) throws Exception {
        int code = this.profiles.nameIsValid(player.getPlayerName());
        if (code == 1) {
            player.setPlayerID(this.profiles.getPlayerListSize() + 1);
            player.setStats(new Statistics());
        } else if (code == -1) {
            throw new Exception("Name already used. Please try again"); //todo create different exception?
        } else if (code == -2) {
            throw new Exception("Name needs to have at least 2, and a maximum of 10 letters");
        }
        this.profiles.addPlayer(player);
    }

    /**
     * Renames a player.
     * @param oldPlayerName Is the {@code Player} which will be renamed.
     * @param newPlayerName Is the new name for the player.
     */
    public void renamePlayer(String oldPlayerName, String newPlayerName) throws Exception {
        if(this.profiles.nameIsValid(newPlayerName) == -1 && !oldPlayerName.equals(newPlayerName)){
            throw new Exception("Name already exists!");
        }
        this.profiles.getPlayerWithName(oldPlayerName).setPlayerName(newPlayerName);
    }

    public void deletePlayer(String playerName) throws Exception {
        if(this.profiles.getPlayerListSize() == 1) {
            throw new Exception("One Profile needs to exist.");
        }
        if(currentPlayer.getPlayerName().equals(playerName)){
            throw new Exception("Chosen profile is currently logged in. Change Profile to delete it.");
        }
        this.profiles.removePlayerFromList(playerName);
    }

    /**
     * If the jsonString is == "", profiles will be set to null, so the main activity knows, that the User needs to create a player/profile.
     * @param jsonString will be passed by the MainActivity. Contains the data, which is stored in the SharedPreferences.
     *                   Passes an empty String "", if the App is being opened for the first time and therefore doesn't have any profiles/stored data.
     */
    public void loadFromJson(String jsonString) {
        if(jsonString.equals("")) this.profiles = null;
        this.profiles = this.gson.fromJson(jsonString, Profile.class);

    }


    public String saveToJson() {
        return this.gson.toJson(profiles);
    }

    public void setNewProfile(){
        this.profiles = new Profile();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DEFAULTPATH);
    }
}
