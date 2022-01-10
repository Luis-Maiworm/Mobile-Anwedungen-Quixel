package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizz.R;


/**
 * NOT IMPLEMENTED | This Screen would replace the current Endscreen with a better
 * Endscreen. This screen would display the Score of the other player and
 * the Winner of the round.
 */
public class EndscreenMPActivity extends AppCompatActivity {

    private TextView finalText, scorePlayer1, scorePlayer2, winner;
    private Button goBackHome;
    private int score1, score2;
    private String p1, p2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen_mpactivity);
    }

    private void setup() {
        finalText.findViewById(R.id.roundOverLabelMP);
        scorePlayer1.findViewById(R.id.finalScoreLabel1);
        scorePlayer2.findViewById(R.id.finalScoreLabel2);
        goBackHome.findViewById(R.id.goToHomeBtnMP);
    }

    private void receiveData() {
    }

    @Override
    public void onBackPressed() {
    }
}