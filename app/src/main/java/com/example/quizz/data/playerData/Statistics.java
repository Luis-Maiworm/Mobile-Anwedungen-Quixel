package com.example.quizz.data.playerData;

import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Contains the Stats which later will be displayed and stored.
 * Every {@code Statistics} will have a corresponding {@link Player} Object
 * It also extracts the {@link Categories} List, to give {@code Category}-based Statistics.
 * Furthermore, it offers incrementMethods to every Stat, to operate easier.
 *
 * It may also contain methods, which are reponsible to process the values, turning them into percentages,
 * or take "rightQuestions" and "wrongQuestions" and make a general balance.
 */

public class Statistics implements Serializable {


    /*  public HashMap<String, Integer> getRightQuestionsPerCat() {
        return rightQuestionsPerCat;
   }
    public void setRightQuestionsPerCat(HashMap<String, Integer> rightQuestionsPerCat) {
        this.rightQuestionsPerCat = rightQuestionsPerCat;
    }

    */

    /**
     * Right and wrong answers. Can be filtered with Category, or just return total number of
     * right/wrong or just total answered questions.
     */

    private int highScore = 0;
    private HashMap<Integer, List<Integer>> gamesMultiplayer = new HashMap<>();

    public List<Integer> timeLeft = new ArrayList<>(); //index 0: wie viel Zeit verfügbar war, index 1: wie viel Zeit am ende übrig war.


    private List<Integer> answersTotal = new ArrayList<>(
            Arrays.asList(0, 0)
    );
    private HashMap<Categories, List<Integer>> answersPerCategory = new HashMap<>();
    private HashMap<Difficulties, List<Integer>> answersPerDifficulty = new HashMap<>();
    private HashMap<Types, List<Integer>> answersPerType = new HashMap<>();


    /**
     *
     */
    public Statistics(){
        //todo fill hashmap
        for(Categories c : Categories.values()){
            List<Integer> temp = new ArrayList<>();
            temp.add(0); temp.add(0);
            answersPerCategory.put(c, temp);
        }
        for(Difficulties d : Difficulties.values()){
            List<Integer> temp2 = new ArrayList<>();
            temp2.add(0); temp2.add(0);
            answersPerDifficulty.put(d, temp2);
        }
        for(Types t : Types.values()){
            List<Integer> temp3 = new ArrayList<>();
            temp3.add(0); temp3.add(0);
            answersPerType.put(t, temp3);
        }


    }

    /**
     * Liste
     * List<Integer> total
     * -> total berechenbar durch alle 3 Listen -> eine Frage muss immer aus mindestens einem der Argumente
     * bestehen -> man am besten nimmt man die List<Type> da diese nur 2 Möglichkeiten hat
     * List<Categories, List<Integer>> perCat
     * List<Type, List<Integer>> perType
     * List<Difficulty, List<Integer>> perDiff
     *
     * afterQuestion -> incrementet alle HashMaps / Lists gleichzeitig
     * @param highScore
     */



    public void setHighScore(int highScore){
        this.highScore = highScore;
    }
    public int getHighScore(){
        return this.highScore;
    }




    //getter


    public int getTotalAnswers(){
        return this.answersTotal.get(0) + this.answersTotal.get(1);
    }

    public int getTotalWrongAnswers(){
        return this.answersTotal.get(1);
    }
    public int getTotalRightAnswers(){
        return this.answersTotal.get(0);
    }

    public int getAnswerPerCategory(Categories c, boolean isCorrect){
        if(isCorrect) return this.answersPerCategory.get(c).get(0);
        return this.answersPerCategory.get(c).get(1);
    }

    public int getAnswerPerDifficulty(Difficulties d, boolean isCorrect) {
        if(isCorrect) return this.answersPerDifficulty.get(d).get(0);
        return this.answersPerDifficulty.get(d).get(1);
    }

    public int getAnswerPerType(Types t, boolean isCorrect) {
        if(isCorrect) return this.answersPerType.get(t).get(0);
        return this.answersPerType.get(t).get(1);
    }

    //Incrementers

    public void incrementTotal(boolean isCorrect){
        if(isCorrect) this.answersTotal.set(0, this.answersTotal.get(0) + 1);
        if(!isCorrect) this.answersTotal.set(1, this.answersTotal.get(1) + 1);
    }


    public void incrementAnswerPerCategory(Categories c, boolean isCorrect){
        this.answersPerCategory.put(c, incrementList(this.answersPerCategory.get(c), isCorrect));
    }

    public void incrementAnswerPerDifficulty(Difficulties d, boolean isCorrect){
        this.answersPerDifficulty.put(d, incrementList(this.answersPerDifficulty.get(d), isCorrect));
    }

    public void incrementAnswerPerType(Types t, boolean isCorrect){
        this.answersPerType.put(t, incrementList(this.answersPerType.get(t), isCorrect));
    }



    private List<Integer> incrementList(List<Integer> value, boolean isCorrect){
        if(isCorrect) value.set(0, value.get(0) + 1);
        if(!isCorrect) value.set(1, value.get(1) + 1);
        return value;
    }



}