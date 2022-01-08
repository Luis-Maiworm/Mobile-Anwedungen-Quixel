package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.quizz.R;


/**
 * NOT FINISHED |
 */
public class WaitingForOtherPlayerActivity extends AppCompatActivity {

    private int score1, score2;
    private final long timeout = 20000;
    private long endTime;
    private String p1, p2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_other_player);


       waitForPlayer();
       test();

        }

    private void test() {
    }


    private void waitForPlayer() {

            while(!getIntent().hasExtra("flag1") && !getIntent().hasExtra("flag2") && System.currentTimeMillis() < endTime ) {
                endTime = System.currentTimeMillis() + timeout;
                System.out.println(endTime);
            }

        }


        //score1 = getIntent().getIntExtra("score1", 0);
        //score2 = getIntent().getIntExtra("score2", 0);


    @Override
    public void onBackPressed() {
    }
}
    
    
    
    
    
