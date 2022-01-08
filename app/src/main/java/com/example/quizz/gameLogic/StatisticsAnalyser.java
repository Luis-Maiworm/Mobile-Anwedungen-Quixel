package com.example.quizz.gameLogic;

import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.Statistics;
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

    public final boolean ASC = true;
    public final boolean DESC = false;

    HashMap<Categories, Double> categoriesPercentageList = new HashMap<>();

    /**
     *
     * @param currentPlayer to set the Statistics to this player.
     */
    public StatisticsAnalyser(Player currentPlayer){
        this.s = currentPlayer.getStats();
    }

    /**
     * Updates the Player which have been given as parameter into the {@code StatisticsAnalyser's Constructor}.
     * @return the player, which statistics have been changed by the {@code StatisticsAnalyser}.
     */
    public void afterQuestion(Question q){
        s.incrementTotal(q.isCorrect());
        s.incrementAnswerPerCategory(q.getCategory(), q.isCorrect());
        s.incrementAnswerPerDifficulty(q.getDifficulty(), q.isCorrect());
        s.incrementAnswerPerType(q.getType(), q.isCorrect());
    }

    public void afterRound(List<Question> qList, String identifier){
        switch(identifier){
            case "Gamemode_standard":
            case "Gamemode_configurable":
                for(Question q : qList){
                    afterQuestion(q);
                }
                break;
            case "Gamemode_endless":
                break;
        }
    }
    public void afterRound(List<Question> qList){
                for(Question q : qList){
                    afterQuestion(q);
                }
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



    public HashMap<Categories, Double> percentageList(boolean includingEmpty, boolean order){
        for(Categories c : Categories.values()){
            double ratio = answerRatioPerCategory(c);
            if(includingEmpty) {
                categoriesPercentageList.put(c, ratio);
            }
            else {
                if (!Double.isNaN(ratio)) categoriesPercentageList.put(c, ratio);           // if the ratio is NOT A NUMBER, put the value+category in the map -> gives us a list with every category  used
            }
        }
        return returnSortedList(categoriesPercentageList, order);
    }

    private static HashMap<Categories, Double> returnSortedList(HashMap<Categories, Double> unsortedMap, boolean order){
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

}