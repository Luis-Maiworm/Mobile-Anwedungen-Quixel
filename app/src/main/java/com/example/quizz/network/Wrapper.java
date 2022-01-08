package com.example.quizz.network;

import com.example.quizz.data.playerData.Player;
import com.example.quizz.questionManager.Question;
import java.io.Serializable;
import java.util.ArrayList;

public class Wrapper implements Serializable {

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    Player player;

    public ArrayList<Question> getqList() {
        return qList;
    }

    public void setqList(ArrayList<Question> qList) {
        this.qList = qList;
    }

    ArrayList<Question> qList = new ArrayList<>();
}