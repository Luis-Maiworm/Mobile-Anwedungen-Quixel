package com.example.quizz.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.Statistics;
import com.example.quizz.gameLogic.PlayerManager;
import com.example.quizz.view.fragments.LoginFragment;
import com.example.quizz.view.fragments.ShowPlayerFragment;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button openSingleplayer, openMultiplayer, openStats, saveStats, resetStats;
    private ImageButton openProfileChooser;
    private ImageButton openPlayerView;
    private TextView playerName;
    private PlayerManager pManager = new PlayerManager();
    private LoginFragment choosePlayerFragment = new LoginFragment();
    private ShowPlayerFragment playerFragment = new ShowPlayerFragment();
    private SharedPreferences pref;
    private SharedPreferences.Editor ed;
    private Player currentPlayer = new Player();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        passManagerToFragments();
        setUpOnClicks();
        checkForNewData();
        loadData();
    }

    /**
     * Sets variables and initializes the View
     */
    public void initVariables() {
        pref = getSharedPreferences(Constants.appConstant, 0);

        // **************Debug Buttons**************
        saveStats = findViewById(R.id.saveStats);
        resetStats = findViewById(R.id.resetStats);
        saveStats.setVisibility(View.INVISIBLE);
        resetStats.setVisibility(View.INVISIBLE);
        //******************************************
        // Buttons
        openSingleplayer = findViewById(R.id.singleplayerBtn);
        openMultiplayer = findViewById(R.id.multiplayerBtn);
        openStats = findViewById(R.id.statsBtn);
        openProfileChooser = findViewById(R.id.fragmentBtn);
        playerName = findViewById(R.id.mainProfileLabel);
        openPlayerView = findViewById(R.id.mainProfileIcon);





    }

    /**
     * Checks if there is any new intent after the main activity is created. This is usually the case
     * after a game loop is finished and the player returns to the main menu.
     * After receiving the updated player, the global player and playerManager instance gets updated.
     * Lastly the Player Stats will be saved persistently.
     */
    private void checkForNewData() {
        if (getIntent().hasExtra(Constants.playerConstant)) {
            loadData();
            currentPlayer = (Player) getIntent().getSerializableExtra(Constants.playerConstant);
            pManager.saveCurrentPlayer(currentPlayer, currentPlayer.getPlayerName());
            pManager.setCurrentPlayer(currentPlayer);
            saveStats();
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads the data from SharedPreferences and updates a few UI Elements
     */
    private void loadData() {
        //Loads the data from the playerManager
        pManager.loadFromJson(pref.getString(Constants.statsConstant, ""));
        // when there are no profiles (JSON empty) a new profile will be created -> opens the relevant fragments
        if (pManager.getProfiles() == null) {
            openProfileChooser.setImageResource(R.drawable.ic_baseline_close_24);
            pManager.setNewProfile();
            openFragmentFirstTime();
            // Updates instances
            pManager.setCurrentPlayer(currentPlayer);
            currentPlayer.setStats(new Statistics());
            // When there is at least one profile, the view gets updated for the current player, which is currently saved in the SharedPreferences
        } else {
            pManager.setCurrentPlayer(pManager.getProfiles().getPlayerWithName(pManager.getProfiles().getCurrentPlayer()));
            currentPlayer = pManager.getCurrentPlayer();
            playerName.setText(currentPlayer.getPlayerName());
            openPlayerView.setImageResource(currentPlayer.getPlayerIcon());

        }
    }

    /**
     * Transfers all needed managers to the fragments
     */
    private void passManagerToFragments() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.playerManagerConstant, pManager);
        //sets Arguments for both fragments (playerView and playerChooser)
        choosePlayerFragment.setArguments(bundle);
        playerFragment.setArguments(bundle);
    }

    /**
     * When the fragments are opened for the first time
     */
    private void openFragmentFirstTime() {
        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.FrameLayout, choosePlayerFragment);
        fT.commit();             // so the app knows that the fragment has been set and is != null
    }

    /**
     * onClickListeners
     */
    private void setUpOnClicks() {
        //sets onClick listener for the buttons
        openSingleplayer.setOnClickListener(this);
        openMultiplayer.setOnClickListener(this);
        openStats.setOnClickListener(this);
        // openSettings.setOnClickListener(this);
        openProfileChooser.setOnClickListener(this);
        openPlayerView.setOnClickListener(this);
        saveStats.setOnClickListener(this);
        resetStats.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    // Main Menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.singleplayerBtn:
                startSingleplayer();
                break;
            case R.id.multiplayerBtn:
                startMultiplayer();
                break;
            case R.id.statsBtn:
                startStats();
                break;
            case R.id.fragmentBtn:
                fragmentBtn();
                break;
            case R.id.mainProfileIcon:
                if (playerFragment.getView() != null) {
                    deleteCurrentPlayerFrag();
                } else {
                    openCurrentPlayerFrag();
                }
                break;
            case R.id.saveStats:
                saveStats();
                break;
            case R.id.resetStats:
                resetStats();
                break;
            default:
                break;
        }
    }

    /**
     * Starts the Singleplayer Mode
     */
    private void startSingleplayer() {
        saveStats();
        Intent toSingleplayer = new Intent(MainMenuActivity.this, ChooseGamemodeActivity.class);
        toSingleplayer.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toSingleplayer);
    }

    /**
     * Starts the Multiplayer Mode
     */
    private void startMultiplayer() {
        saveStats();
        Intent toMultiplayer = new Intent(MainMenuActivity.this, MultiplayerActivity.class);
        toMultiplayer.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toMultiplayer);
    }

    /**
     * Starts the Stats Screen
     */
    private void startStats() {
        Intent toStats = new Intent(MainMenuActivity.this, StatisticsActivity.class);
        // statisticsSimulation(); // Integrationstest
        currentPlayer = pManager.getCurrentPlayer();
        toStats.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toStats);
    }

    /**
     * SharedPreferences:
     * Saves the stats of the current player with SharedPreferences
     */
    private void saveStats() {
        ed = pref.edit();
        ed.putString(Constants.statsConstant, pManager.saveToJson());
        ed.apply();
    }

    /**
     * DEBUG UI Methode
     * SharedPreferences:
     * Resets all stats
     */
    private void resetStats() {
        ed = pref.edit();
        pManager.getCurrentPlayer().setStats(new Statistics());
        ed.putString(Constants.statsConstant, pManager.saveToJson());
        ed.apply();
    }


    /**********************************************************************************
     * Integrationstest-Methode für StatisticsAnalyser
     * ---------------------------------------------------
     * Simuliert eine Auswertung von einer Fragenliste durch die Befüllung der entsprechenden
     * Listen, welche die Werte aus den beantworteten Fragen bekommen würden. Daruch konnte
     * überprüft werden, ob die Statistiken korrekt im Statistics Screen dargestellt werden.
     **********************************************************************************/
    public void statisticsSimulation() {
        Statistics stats = currentPlayer.getStats();
        for (int i = 0; i < 1000; i++) {
            int rCat = (int) (Math.random() * Categories.values().length);
            int j = 0;
            for (Categories c : Categories.values()) {
                if (j == rCat) {
                    boolean boo = Math.random() < 0.5;
                    stats.incrementAnswerPerCategory(c, boo);
                    stats.incrementTotal(boo);
                }
                j++;
            }
        }
    }
    //***************************************************************************************


    public void fragmentBtn() {
        if (choosePlayerFragment.getView() != null) {
            if (currentPlayer == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("");
                builder.setPositiveButton("Okay", (dialog, id) -> {
                });
                builder.show();
            } else {
                if (pManager.getProfiles().getPlayerListSize() == 1) {
                    pManager.chooseCurrentPlayer(pManager.getProfiles().getPlayerList().get(0).getPlayerName());
                    currentPlayer = pManager.getCurrentPlayer();
                    playerName.setText(currentPlayer.playerName);
                    openPlayerView.setImageResource(currentPlayer.getPlayerIcon());
                    deleteOpenChooseProfileFrag();
                }
            }
        } else if (playerFragment.getView() != null) {
            deleteCurrentPlayerFrag();
        } else {
            openChooseProfileFrag();
        }
    }

    public void openFragment(Fragment frag, int... anim) {

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);     //reordering? true or false
        if (anim.length != 0) {
            fT.setCustomAnimations(anim[0], anim[1]);
        }
        fT.replace(R.id.FrameLayout, frag);
        fT.commit();
        ImageViewAnimatedChangeIn(this, openProfileChooser, R.drawable.ic_baseline_close_24);
    }

    public void closeFragment(Fragment frag, int... anim) {

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);     //reordering? true or false
        if (anim.length > 1) {
            fT.setCustomAnimations(anim[0], anim[1]);
        }
        fT.remove(frag);
        fT.commit();

        if (!playerName.getText().toString().equals(currentPlayer.getPlayerName())) {
            playerName.setText(currentPlayer.playerName);
        }
        ImageViewAnimatedChangeOut(this, openProfileChooser, R.drawable.ic_baseline_check_circle_24);
    }

    public void openCurrentPlayerFrag() {
        openFragment(playerFragment, R.anim.scale_up, R.anim.scale_down);
    }

    public void deleteCurrentPlayerFrag() {

        closeFragment(playerFragment, R.anim.scale_up, R.anim.scale_down);


    }

    public void openChooseProfileFrag() {
        openFragment(choosePlayerFragment, R.anim.scale_up, R.anim.scale_down);
    }

    public void deleteOpenChooseProfileFrag() {
        closeFragment(choosePlayerFragment, R.anim.scale_up, R.anim.scale_down);
    }

    public static void ImageViewAnimatedChangeIn(Context c, ImageButton v, int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public static void ImageViewAnimatedChangeOut(Context c, ImageButton v, int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public void onBackPressed() {
        if (choosePlayerFragment.getView() != null) {
            if (currentPlayer == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("");
                builder.setPositiveButton("Okay", (dialog, id) -> {
                });
                builder.show();
            } else {
                if (pManager.getProfiles().getPlayerListSize() == 1) {
                    pManager.chooseCurrentPlayer(pManager.getProfiles().getPlayerList().get(0).getPlayerName());
                    currentPlayer = pManager.getCurrentPlayer();
                    playerName.setText(currentPlayer.playerName);
                    openPlayerView.setImageResource(currentPlayer.getPlayerIcon());
                    deleteOpenChooseProfileFrag();
                }
            }
        } else if (playerFragment.getView() != null) {
            deleteCurrentPlayerFrag();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Do you really want to quit?");
            builder.setPositiveButton(R.string.quitYes, (dialog, id) -> {
                finish();
            });
            builder.setNegativeButton(R.string.QuitCancel, (dialog, id) -> {
            });
            builder.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * In case something went wrong, the stats will still be saved after the activity gets destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveStats();
    }
}

