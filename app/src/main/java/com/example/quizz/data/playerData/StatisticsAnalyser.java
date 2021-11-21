package com.example.quizz.data.playerData;


import com.example.quizz.data.enums.Categories;
import com.example.quizz.questionManager.Question;
import com.example.quizz.questionManager.QuestionManager;


import java.util.HashMap;
import java.util.List;

/**
 * The {@code StatisticsAnalyser} automatically sets the information gathered when a {@link Question} has been answered.
 * It also processes the data, so in the Statistics Menu it doesn't just show Integer values.
 */

public class StatisticsAnalyser {

    Statistics s;
    Question q;
    Player p;
    QuestionManager qManager = new QuestionManager();


    /**
     * Ablauf:
     * Übergebener Spieler (currentPlayer) wird auf einen temporären Übertragen. Diese Instanz hier "p"
     * wird so verändert wie sie es soll. Also entweder am Ende einer Runde, oder einer Frage.
     *
     * Bei Frage: eine der "takeQuestion" Methoden.
     * Bei Runde: eine der "takeQuestions" Methoden.
     *
     * Am Ende wird die Methode "getUpdatedPlayer" aufgerufen, die die bearbeitete und geupdatete Instanz "p" als Player zurückgibt.
     * Diese soll auf den currentPlayer übertragen werden.
     *
     * Verständnis:
     * Ab da, wo der StatisticsAnalyser angelegt wird, verändern sich die ORIGINALEN Stats des currentPlayer NICHT.
     * Erst wenn die Methode getUpdatedPlayer aufgerufen wird -> werden die aktualisierten Statistiken übergeben.
     * In dem Fall kann die Instanz des StatisticsAnalyser einfach weitergegeben werden.
     *
     * Andere Möglichkeit wäre:
     * currentPlayer muss jedesmal übergeben werden (über setter Methode möglich, oder über Konstruktor)
     * und anschließend geupdatet werden -> aufwendig -> Vorteil: Spieler bleibt fast IMMER aktuell... ist das nötig?
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


    /*
    public Player getUpdatedPlayer(){
        p.setStats(s);
        return this.p;
    }

    public Player updatedPlayer(){
        p.setStats(s);
        return this.p;
    }
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
    public HashMap<Categories, Double> percentageList(){
        for(Categories c : Categories.values()){
            categoriesPercentageList.put(c, answerRatioPerCategory(c));
        }
        return categoriesPercentageList;
    }

    //todo sort


    /**
     * Switch case to determine the gamemode. Then iterate through all answered Questions and evaluate with "afterQuestion" method
     * -> also determines which "afterQuestion" method should be used.
     * -> only when gamemode includes additional or less parameters (like timemode needs -> an integer parameter showing how much time was left when question answered)
     * Time Gamemode: Should include the possibility to set time with constructor
     */

    /*
    public Player afterRound(List<Question> qList, GameSettings mode, boolean gWon){

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