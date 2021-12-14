package com.example.quizz.data.playerData;


import com.example.quizz.data.enums.Categories;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The {@code StatisticsAnalyser} automatically sets the information gathered when a {@link Question} has been answered.
 * It also processes the data, so in the Statistics Menu it doesn't just show Integer values.
 *
 * It takes a Player and modifies/reads the Statistics of it.
 *
 * The class is independent and can be created everytime it's needed. Doesn't need to get passed through the whole lifecycle!
 *
 */

public class StatisticsAnalyser {

    Statistics s;
    Question q;
    Player p;
    QuestionManager qManager = new QuestionManager();

    public final boolean ASC = true;
    public final boolean DESC = false;


    /**
     * Ablauf:
     * Übergebener Spieler (currentPlayer) wird auf einen temporären Übertragen. Diese Instanz hier "p"
     * wird so verändert wie sie es soll. Also entweder am Ende einer Runde, oder einer Frage.
     *
     * Bei Frage: eine der "takeQuestion" Methoden.
     * Bei Runde: eine der "takeQuestions" Methoden.
     */



    /**
     *
     * @param currentPlayer to set the Statistics to this player.
     */
    public StatisticsAnalyser(Player currentPlayer){
        this.p = currentPlayer;
        this.s = currentPlayer.getStats();
    }

    /**
     * Updates the Player which have been given as parameter into the {@code StatisticsAnalyser's Constructor}.
     * @return the player, which statistics have been changed by the {@code StatisticsAnalyser}.
     */


    public void afterQuestion(Question q, boolean qCorrect){
        s.incrementTotal(qCorrect);
        s.incrementAnswerPerCategory(q.getCategory(), qCorrect);
        s.incrementAnswerPerDifficulty(q.getDifficulty(), qCorrect);
        s.incrementAnswerPerType(q.getType(), qCorrect);
    }

    public void afterQuestion(Question q, boolean qCorrect, int remainingTimer){
        s.incrementTotal(qCorrect);
        s.incrementAnswerPerCategory(q.getCategory(), qCorrect);
        s.incrementAnswerPerDifficulty(q.getDifficulty(), qCorrect);
        s.incrementAnswerPerType(q.getType(), qCorrect);
    }



    /**
     *
     * @return percentage of right answers of ALL categories.
     */
    public double totalAnswerRatio() {
        double totalRightAnswers = s.getTotalRightAnswers();
        double totalAnswers = s.getTotalAnswers();
        return (totalRightAnswers / totalAnswers) * 100;
    }


    public double answerRatioPerCategory(Categories category){
        double rightAnswers = s.getAnswerPerCategory(category, true);
        double wrongAnswers = s.getAnswerPerCategory(category, false);
        return (rightAnswers / (rightAnswers + wrongAnswers)) * 100;
    }

    HashMap<Categories, Double> categoriesPercentageList = new HashMap<>();

    //liste mit allen answerRatios. -> am besten sortiert, sodass eine auflistung der besten kategorien möglich ist
    public HashMap<Categories, Double> percentageList(boolean includingEmpty, boolean order){        //todo variante: noch nie gespielte kategorien -> werden gar nicht erst ausgegeben
        for(Categories c : Categories.values()){
            double ratio = answerRatioPerCategory(c);
            if(includingEmpty) {
                categoriesPercentageList.put(c, ratio);
            }
            else {
                if (!Double.isNaN(ratio)) categoriesPercentageList.put(c, ratio);           // if the ratio is NOT A NUMBER, put the value+category in the map -> gives us a list with every category used
            }

        }

        return returnSortedList(categoriesPercentageList, order);
    }


    public static HashMap<Categories, Double> returnSortedList(HashMap<Categories, Double> unsortedMap, boolean order){
        List<Map.Entry<Categories, Double>> list = new LinkedList<Map.Entry<Categories, Double>>(unsortedMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Categories, Double>>() {
            public int compare(Map.Entry<Categories, Double> o1, Map.Entry<Categories, Double> o2) {
                if(order){
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        HashMap<Categories, Double> sortedMap = new LinkedHashMap<Categories, Double>();

        for(Map.Entry<Categories, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;

    }





    /**
     * Switch case to determine the gamemode. Then iterate through all answered Questions and evaluate with "afterQuestion" method
     * -> also determines which "afterQuestion" method should be used.
     * -> only when gamemode includes additional or less parameters (like timemode needs -> an integer parameter showing how much time was left when question answered)
     * Time Gamemode: Should include the possibility to set time with constructor
     */

    /*
    public Player afterRound(List<Question> qList, GameSettings mode){

        switch(mode){

            for(Question q : qList){
                s.afterQuestion(c, booleanIsCorrect)
            }
        }


    return this.p;
    }
    */


    //todo übernimmt zusätzlich den GameMode, damit hier auch bestimmte Statistiken abgerufen werden können
    //todo switch case und nach GameSettings switchen


}