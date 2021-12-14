package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;

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

import com.example.quizz.R;
import com.example.quizz.data.enums.Categories;
import com.example.quizz.data.enums.Difficulties;
import com.example.quizz.data.enums.Types;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.data.playerData.Statistics;
import com.example.quizz.data.playerData.StatisticsAnalyser;
import com.example.quizz.fragments.LoginFragment;
import com.example.quizz.fragments.ShowPlayerFragment;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button openSingleplayer, openMultiplayer, openStats;
    ImageButton openProfileChooser;
    ImageButton openPlayerView;
    TextView playerName;

    PlayerManager pManager = new PlayerManager();
    LoginFragment fragment = new LoginFragment();
    ShowPlayerFragment playerFragment = new ShowPlayerFragment();

    SharedPreferences pref;
    SharedPreferences.Editor ed;

    private Player currentPlayer;


    //fix variables und view variables werden gesetzt
    public void initVariables(){
        pref = getSharedPreferences("MYSTATS", 0);

        openSingleplayer = findViewById(R.id.singleplayerBtn);
        openMultiplayer = findViewById(R.id.multiplayerBtn);
        openStats = findViewById(R.id.statsBtn);
        // Button openSettings = findViewById(R.id.statsBtn);
        openProfileChooser = findViewById(R.id.imageButton);

        playerName = findViewById(R.id.mainProfileLabel);
        openPlayerView = findViewById(R.id.mainProfileIcon);
    }
    private void loadData(){
        //lädt daten aus shared preferences, setzt ggf. views

        //daten werden in den pManager geladen.
        pManager.loadFromJson(pref.getString("PROFILES", ""));
        //wenn kein Profile existiert (wenn die json also leer ist), wird eins erstellt -> die fragments dafür werden geöffnet
        if(pManager.getProfiles()==null){
            pManager.setNewProfile();
            openFragmentFirstTime();

          //  currentPlayer = pManager.getCurrentPlayer();  // würde null ergeben!
          //  System.out.println("!!!!!!!!!!!!" + currentPlayer);
            //geht nur darum die Klassenvariable "currentPlayer" mit der Instanz ausm pManager zu connecten
            // diese Connection kann erst aufgebaut werden, wenn die Instanz im pManager != null ist
            //problem: currentPlayer kann hier NICHT gesetzt werden -> da die Referenz des currentPlayer zudiesem Zeitpunkt == null ist.
            // Lösung -> dort wo der currentPlayer gebraucht wird muss dieser erneut durch currentPlayer = pManager.getCurrentPlayer() gesetzt werden.
            // oder: es wird straigh up immer nur pManager.getCurrentPlayer() verwendet.
            //im Endeffekt wird dann nur die Klassenvariable nicht perfekt einsatzbereit sein, das wars aber auch

        } else{     //ansonsten wird die View für den CurrentPlayer gesetzt, welcher in den SharedPreferences gespeichert ist
            pManager.setCurrentPlayer(pManager.getProfiles().getPlayerWithName(pManager.getProfiles().getCurrentPlayer()));
            currentPlayer = pManager.getCurrentPlayer();
            playerName.setText(currentPlayer.getPlayerName());
            openPlayerView.setImageResource(currentPlayer.getPlayerIcon());
        }
    }
    private void passManagerToFragments(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("playerManager", pManager);
        //sets Arguments for both fragments (playerView and playerChooser)
        fragment.setArguments(bundle);
        playerFragment.setArguments(bundle);
    }
    private void openFragmentFirstTime(){
        // wird in loadData aufgerufen
        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.FrameLayout, fragment);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        loadData();
        passManagerToFragments();
        setUpOnClicks();
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
            case R.id.imageButton:
                if(fragment.getView() != null){
                    deleteOpenChooseProfileFrag();
                } else{
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

            default:
                break;
        }
    }


    public void startSingleplayer() {
        Intent toSingleplayer = new Intent(MainMenuActivity.this, ChooseCategoryActivity.class);
        toSingleplayer.putExtra("player", currentPlayer);
        startActivity(toSingleplayer);
    }

    public void startMultiplayer() {        //todo aktuell noch stats speichern
       // SharedPreferences.Editor ed = pref.edit();
        ed = pref.edit();
        ed.putString("PROFILES", pManager.saveToJson());
        ed.apply();
    }
    public void startStats() {
        Intent toStats = new Intent(MainMenuActivity.this, StatisticsActivity.class);

        currentPlayer = pManager.getCurrentPlayer();

        //todo instanz im statsActivity ist nicht die gleiche wie hier!


        setStatsTest();

        toStats.putExtra("player", pManager.getCurrentPlayer());


        startActivity(toStats);

    }

    //set custom stats
    public void setStatsTest(){
        Statistics stats = pManager.getCurrentPlayer().getStats();

        for(int i = 0; i < 1000; i++){
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

    public void openCurrentPlayerFrag(){
        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.FrameLayout, playerFragment);
        fT.commit();
    }
    public void deleteCurrentPlayerFrag(){
        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.remove(playerFragment);
        fT.commit();
    }

    public void openChooseProfileFrag(){
        ImageButton temp = (ImageButton) findViewById(R.id.imageButton);

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up_profilefrag, R.anim.scale_down_profilefrag);
        fT.replace(R.id.FrameLayout, fragment);
        fT.commit();

        ImageViewAnimatedChangeIn(this, temp, R.drawable.ic_baseline_close_24);
    }
    public void deleteOpenChooseProfileFrag() {
        ImageButton temp = (ImageButton) findViewById(R.id.imageButton);

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up_profilefrag, R.anim.scale_down_profilefrag);
        fT.remove(fragment);
        fT.commit();

        ImageViewAnimatedChangeOut(this, temp, R.drawable.ic_baseline_check_circle_24);
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
    public static void ImageViewAnimatedChangeOut(Context c, ImageButton v, int new_image){     //todo ImageButton war ImageView (falls problem weischt bescheid)
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




}
