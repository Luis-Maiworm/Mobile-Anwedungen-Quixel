package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.fragments.LoginFragment;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button openSingleplayer, openMultiplayer, openStats;
    ImageButton openProfileChooser;

    // Global Player
    private Player p1;

    PlayerManager pManager = new PlayerManager();

    LoginFragment fragment = new LoginFragment();

    public static boolean flag = false;     // könnte überprüfen ob eine activity schonmal gestartet wurde
    //todo so auch testbar, ob beim erneuten ActivityStart (falls notwendig) der ProfileChooser erneut aufgerufen werden soll

    TextView testView; //todo weg!


    public void playerSetup(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("playerManager", pManager);

        fragment.setArguments(bundle);

        // Testing

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        System.out.println(pManager);
        playerSetup();





        openProfileChooser = findViewById(R.id.imageButton);

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.FrameLayout, fragment);
        fT.commit();                                        // so the app knows that the fragment has been set and is != null

        openProfileChooser.setOnClickListener(this);

        // View Setup
        openSingleplayer = findViewById(R.id.singleplayerBtn);
        openMultiplayer = findViewById(R.id.multiplayerBtn);
        openStats = findViewById(R.id.statsBtn);
        // Button openSettings = findViewById(R.id.statsBtn);
        openSingleplayer.setOnClickListener(this);
        openMultiplayer.setOnClickListener(this);
        openStats.setOnClickListener(this);
        // openSettings.setOnClickListener(this);
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
                openStatsMenu();
                break;
            case R.id.imageButton:
                if(fragment.getView() != null){
                    deleteProfileFrag();
                } else{
                    openProfileFrag();
                }

            default:
                break;
        }
    }


    public void openStatsMenu() {
        Intent toStats = new Intent(MainMenuActivity.this, StatisticsActivity.class);
        toStats.putExtra("Player", p1);
        startActivity(toStats);
    }
    public void openSettingsMenu() {
    }
    public void startSingleplayer() {
        Intent toSingleplayer = new Intent(MainMenuActivity.this, ChooseCategoryActivity.class);
        toSingleplayer.putExtra("Player", p1);
        startActivity(toSingleplayer);
    }
    public void startMultiplayer() {
    }


    public void openProfileFrag(){
        ImageButton temp = (ImageButton) findViewById(R.id.imageButton);

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up_profilefrag, R.anim.scale_down_profilefrag);
        fT.replace(R.id.FrameLayout, fragment);
        fT.commit();

        ImageViewAnimatedChangeIn(this, temp, R.drawable.ic_baseline_close_24);
    }


    public void deleteProfileFrag() {
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

    public static void ImageViewAnimatedChangeOut(Context c, ImageView v, int new_image){
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
