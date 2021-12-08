package com.example.quizz.gameLogic;

import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.gameLogic.gamemodes.IGamemode;
import com.example.quizz.questionManager.Question;

import java.util.HashMap;
import java.util.List;

/**
 * The {@code StatisticsAnalyser} automatically sets
 * the information gathered when a {@link Question}
 * has been answered. It also processes the data,
 * so in the Statistics Menu it doesn't just
 * show Integer values.
 */
public interface IStatisticsAnalyser {

    /**
     *
     * @param q last active Question
     * @param qCorrect last answer
     */
    public void afterQuestion(Question q, boolean qCorrect);

    /**
     *
     * @param q last active question
     * @param qCorrect last answer
     * @param remainingTimer time time before the question
     *                       was answered
     */
    public void afterQuestion(Question q, boolean qCorrect,
                              int remainingTimer);

    /**
     * @return percentage of right answers of ALL categories.
     */
    public double totalAnswerRatio();

    /**
     * @param category
     * @return percentage of right answers per category
     */
    public double answerRatioPerCategory(Categories category);

    /**
     * list containing all calculated percentages
     */
    public HashMap<Categories, Double> percentageList();

    /**
     * Iterates through all answered Questions and evaluate
     * with "afterQuestion" method
     * -> also determines which "afterQuestion" method should be used.
     * @param qList active questions
     * @param mode active gamemode
     * @param gWon true: game won / false: game lost
     * @return
     */
    public Player afterRound(List<Question> qList, IGamemode mode,
                             boolean gWon);
}
