package com.example.quizz.gameLogic.timerLogic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static com.example.quizz.gameLogic.timerLogic.Status.*;

public class Timer {

    private int startValue;
    private Status status = STOPPED;
    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

    public Timer(int startValue) {
        this.startValue = startValue;

    }
    public void startTimer() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                status = RUNNING;
                System.out.println(startValue);
                startValue--;
                if(startValue < 0) {
                    timer.shutdown();
                    status = STOPPED;
                }


            }
        };
        timer.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        timer.shutdown();
        status = STOPPED;
    }

    public Status getStatus() {
        return status;
    }

    public int getStartValue() {
        return startValue;
    }



}
