package com.example.quizz.gameLogic.timerLogic;

import static com.example.quizz.gameLogic.timerLogic.Status.*;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class Timer {

    /**
     * NOT IN USE ANYMORE
     *
     *
     */

    private Status status = STOPPED;
    private ProgressBar timerBar;
    private CountDownTimer gameTimer;
    private int time;

    public Timer(ProgressBar timerBar, int maxTime) {

        status = RUNNING;
        this.timerBar = timerBar;

        gameTimer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time + 10;
                timerBar.setProgress(time);
            }

            @Override
            public void onFinish() {
                status = STOPPED;

                System.out.println("STOPPED");
            }
        };
    }


    public void start() {
        gameTimer.start();
    }
    public void stop() {
        time = 0;
        timerBar.setProgress(time);
        gameTimer.cancel();
    }







}
