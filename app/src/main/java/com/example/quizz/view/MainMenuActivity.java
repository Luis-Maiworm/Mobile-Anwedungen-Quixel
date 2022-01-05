package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.gameLogic.PlayerManager;
import com.example.quizz.data.playerData.Statistics;
import com.example.quizz.view.fragments.LoginFragment;
import com.example.quizz.view.fragments.ShowPlayerFragment;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button openSingleplayer, openMultiplayer, openStats, saveStats, resetStats;
    ImageButton openProfileChooser;
    ImageButton openPlayerView;
    TextView playerName;

    PlayerManager pManager = new PlayerManager();
    LoginFragment choosePlayerFragment = new LoginFragment();
    ShowPlayerFragment playerFragment = new ShowPlayerFragment();

    SharedPreferences pref;
    SharedPreferences.Editor ed;

    private Player currentPlayer = new Player();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        loadData();
        passManagerToFragments();
        setUpOnClicks();


        System.out.println("CURRENT PLAYER MAIN:" + pManager.getCurrentPlayer().getStats());

    }


    //fix variables und view variables werden gesetzt
    public void initVariables(){
        pref = getSharedPreferences(Constants.appConstant, 0);      //todo replace with file Strings -> set to "MYAPP" -> or app ID

        // Test zwecke
        saveStats = findViewById(R.id.saveStats);
        resetStats = findViewById(R.id.resetStats);
        //
        openSingleplayer = findViewById(R.id.singleplayerBtn);
        openMultiplayer = findViewById(R.id.multiplayerBtn);
        openStats = findViewById(R.id.statsBtn);
        // Button openSettings = findViewById(R.id.statsBtn);
        openProfileChooser = findViewById(R.id.fragmentBtn);

        playerName = findViewById(R.id.mainProfileLabel);
        openPlayerView = findViewById(R.id.mainProfileIcon);
    }

    private void loadData(){
        //lädt daten aus shared preferences, setzt ggf. views

        //daten werden in den pManager geladen.
        pManager.loadFromJson(pref.getString(Constants.statsConstant, ""));      //todo replace with string file -> change to "stats"
        //wenn kein Profile existiert (wenn die json also leer ist), wird eins erstellt -> die fragments dafür werden geöffnet
        if(pManager.getProfiles()==null){
            openProfileChooser.setImageResource(R.drawable.ic_baseline_close_24);
            pManager.setNewProfile();
            openFragmentFirstTime();

            pManager.setCurrentPlayer(currentPlayer);
            currentPlayer.setStats(new Statistics());


            System.out.println("klassenvariable" + currentPlayer);
            System.out.println("manager variable" + pManager.getCurrentPlayer());
            // following: nur drin, damit die spielerreferenz hier, die gleiche ist wie im playerManager
            // dafür dürfen die variablen aber keine null - WErte zurückgeben, um null pointers zu avoiden...DAHER :
            // (es wird also zuerst ein leerer spieler erzeugt, der dem currentPlayer zugewiesen wird) ... dieser wird dann später einfach überschrieben


            //currentPlayer kann hier nicht gesetzt werden (klassen instanz) , läuft die App also zum ersten mal, muss pManager.getCurrentPlayer(); verwendet werden
            //todo leerer Player Construktor um null value zu vermeiden und referenz herzustellen

        } else{     //ansonsten wird die View für den CurrentPlayer gesetzt, welcher in den SharedPreferences gespeichert ist
            pManager.setCurrentPlayer(pManager.getProfiles().getPlayerWithName(pManager.getProfiles().getCurrentPlayer()));
            currentPlayer = pManager.getCurrentPlayer();
            playerName.setText(currentPlayer.getPlayerName());
            openPlayerView.setImageResource(currentPlayer.getPlayerIcon());
        }
    }
    private void passManagerToFragments(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.playerManagerConstant, pManager);
        //sets Arguments for both fragments (playerView and playerChooser)
        choosePlayerFragment.setArguments(bundle);
        playerFragment.setArguments(bundle);
    }
    private void openFragmentFirstTime(){
        // wird in loadData aufgerufen
        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.FrameLayout, choosePlayerFragment);
        fT.commit();                                        // so the app knows that the fragment has been set and is != null
    }
    private void setUpOnClicks(){
        //sets onClick listener for the buttons
        openSingleplayer.setOnClickListener(this);
        openMultiplayer.setOnClickListener(this);
        openStats.setOnClickListener(this);
        // openSettings.setOnClickListener(this);
        openProfileChooser.setOnClickListener(this);
        openPlayerView.setOnClickListener(this);

        saveStats.setOnClickListener(this);
        resetStats.setOnClickListener(this);
    }


    // Menu
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
                // events mit dem imageButton, der wiederverwendet wird
                // kann schöner gelöst werden! UND settingsfragment ist noch nicht drin
                if(choosePlayerFragment.getView() != null){
                    deleteOpenChooseProfileFrag();
                } else if (playerFragment.getView() != null){
                    deleteCurrentPlayerFrag();
                }
                else{
                    openChooseProfileFrag();
                }
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
                System.out.println("lüppen tuts aber");
                break;
            default:
                break;
        }
    }

    public void startSingleplayer() {
        Intent toSingleplayer = new Intent(MainMenuActivity.this, ChooseGamemodeActivity.class);
        toSingleplayer.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toSingleplayer);
    }

    public void startMultiplayer() {        //todo aktuell noch stats speichern
        Intent toMultiplayer = new Intent(MainMenuActivity.this, MultiplayerActivity.class);
        toMultiplayer.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toMultiplayer);

    }

    public void saveStats(){
        ed = pref.edit();
        ed.putString(Constants.statsConstant, pManager.saveToJson());  // save to the key Constants.statsConstant
        ed.apply();
    }

    public void resetStats(){
        ed = pref.edit();
        pManager.getCurrentPlayer().setStats(new Statistics());             //set empty Statistics as new Stats.
        ed.putString(Constants.statsConstant, pManager.saveToJson());
        ed.apply();
    }

    public void startStats() {
        Intent toStats = new Intent(MainMenuActivity.this, StatisticsActivity.class);
        // statisticsSimulation(); //test zwecke, simuliert zufällige statistiken
        currentPlayer = pManager.getCurrentPlayer();
        toStats.putExtra(Constants.playerConstant, pManager.getCurrentPlayer());
        startActivity(toStats);

    }

    /**
     * INTEGRATIONSTEST-METHODE für StatisticsAnalyser
     *
     */
    public void statisticsSimulation(){
        Statistics stats = currentPlayer.getStats();
        for(int i = 0; i < 1000; i++) {
            int rCat = (int) (Math.random() * Categories.values().length);
            int j = 0;
            for(Categories c : Categories.values()){
                if(j==rCat){
                    boolean boo = Math.random() < 0.5;
                    stats.incrementAnswerPerCategory(c, boo);
                    stats.incrementTotal(boo);
                }
                j++;
            }
        }
    }



    public void startSettings() {
        //todo main settings als activity -> "quickSettings" als Fragment

    }

    public void openFragment(Fragment frag, int ... anim){

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);     //reordering? true or false
        if(anim.length != 0){
            fT.setCustomAnimations(anim[0], anim[1]);
        }
        fT.replace(R.id.FrameLayout, frag);
        fT.commit();

        //todo add boolean to determine if its connected to the button

        ImageViewAnimatedChangeIn(this, openProfileChooser, R.drawable.ic_baseline_close_24);
    }

    public void closeFragment(Fragment frag, int ... anim){

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);     //reordering? true or false
        if(anim.length > 1){
            fT.setCustomAnimations(anim[0], anim[1]);
        }
        fT.remove(frag);
        fT.commit();

        //ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this .findViewById(android.R.id.content)).getChildAt(0);
        //add greyscale when fragment is open.

        ImageViewAnimatedChangeOut(this, openProfileChooser, R.drawable.ic_baseline_check_circle_24);
    }

    public void openCurrentPlayerFrag(){
        openFragment(playerFragment, R.anim.scale_up, R.anim.scale_down);
    }
    public void deleteCurrentPlayerFrag(){

        closeFragment(playerFragment, R.anim.scale_up, R.anim.scale_down);

    }

    public void openChooseProfileFrag(){
        openFragment(choosePlayerFragment, R.anim.scale_up, R.anim.scale_down);
    }
    public void deleteOpenChooseProfileFrag() {
        closeFragment(choosePlayerFragment, R.anim.scale_up, R.anim.scale_down);
    }

    public static void ImageViewAnimatedChangeIn(Context c, ImageButton v, int new_image){
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
    public static void ImageViewAnimatedChangeOut(Context c, ImageButton v, int new_image){
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }




    @Override
    public void onBackPressed() {
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


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUMED");
        saveStats();


        if (getIntent().hasExtra(Constants.playerConstant)) {
            System.out.println("HAT EXTRA");
            currentPlayer = (Player) getIntent().getSerializableExtra(Constants.playerConstant);
            pManager.setCurrentPlayer(currentPlayer);
            System.out.println("RESUMED PLAYER" + currentPlayer);
            saveStats();
        }
        else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("PAUSED");
        saveStats();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}

