package com.example.quizz.gameLogic;

import static org.junit.Assert.*;

import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.Statistics;
import com.example.quizz.questionManager.Question;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatisticsAnalyserTest {

    static StatisticsAnalyser analyser;
    static Player player;
    static Statistics stats;




    @Before
    public void init() {
        player = new Player();
        stats = new Statistics();
        player.setStats(stats);
        analyser = new StatisticsAnalyser(player);
    }


    /**
     * We add one Question, containing different parameters to the method.
     * It then converts the Question and its values into the corresponding statistics.
     * In the following test, we check for the values which should have been changed / incremented.
     */
    @Test
    public void afterQuestion(){
        Question q = new Question();
        fillQuestion(Categories.ANIMALS, Difficulties.HARD, Types.MULTIPLECHOICE, true, q);

        analyser.afterQuestion(q);

        assertEquals(1, player.getStats().getAnswerPerCategory(Categories.ANIMALS, true));
        assertEquals(0, player.getStats().getAnswerPerCategory(Categories.BOOKS, true));
        assertEquals(1, player.getStats().getAnswerPerDifficulty(Difficulties.HARD, true));
        assertEquals(1, player.getStats().getAnswerPerType(Types.MULTIPLECHOICE, true));
        assertEquals(1, player.getStats().getTotalRightAnswers());
        assertEquals(0, player.getStats().getTotalWrongAnswers());
        assertEquals(1, player.getStats().getTotalAnswers());
    }

    @Test
    public void afterQuestion2(){
        Question q = new Question();
        fillQuestion(Categories.BOARDGAMES, Difficulties.EASY, Types.TRUEFALSE, false, q);

        analyser.afterQuestion(q);

        assertEquals(1, player.getStats().getAnswerPerCategory(Categories.BOARDGAMES, false));
        assertEquals(0, player.getStats().getAnswerPerCategory(Categories.ANIMALS, false));
        assertEquals(1, player.getStats().getAnswerPerDifficulty(Difficulties.EASY, false));
        assertEquals(1, player.getStats().getAnswerPerType(Types.TRUEFALSE, false));
        assertEquals(0, player.getStats().getAnswerPerType(Types.MULTIPLECHOICE, false));
        assertEquals(0, player.getStats().getTotalRightAnswers());
        assertEquals(1, player.getStats().getTotalWrongAnswers());
        assertEquals(1, player.getStats().getTotalAnswers());
    }

    /**
     * Method getSomeQuestions() includes a List with 4 Questions, with random set Values.
     * We use the method "afterRound()" of the analyser which analyses the Questions, and transfers the results onto the Player/Statistics instance.
     * We then check if the instance has the right amount of totalRightAnswers, totalWrongAnswers, totalAnswers and f.e. the amount of answers per type (in this case we add the correct and incorrect answers)
     * Due to the fact, that the afterRound method just uses the "afterQuestion" method multiple times, those tests should be enough.
     * Furthermore, those methods are not based on any user input, except the right and wrong answering of a question.
     * The Question values are all set, by the QuestionManager and the API Call.
     * By using enums, we minimize the risk of wrong behaviour of this class.
     */
    @Test
    public void afterRound(){
        analyser.afterRound(getSomeQuestions());


        assertEquals(2, stats.getTotalRightAnswers());
        assertEquals(2, stats.getTotalWrongAnswers());
        assertEquals(4, stats.getTotalAnswers());
        assertEquals(3, stats.getAnswerPerType(Types.TRUEFALSE, true) + stats.getAnswerPerType(Types.TRUEFALSE, false));
    }

    @Test
    public void answerRatioPerCategory(){
        analyser.afterRound(getSomeQuestions());

        assertEquals(50, analyser.answerRatioPerCategory(Categories.COMICS), 0);
        assertEquals(0, analyser.answerRatioPerCategory(Categories.ART), 0);
        assertEquals(100, analyser.answerRatioPerCategory(Categories.BOOKS), 0);

    }

    /**
     * Checks for the correct size of the list. Only categories, which have been answered should be displayed.
     * Due to the fact that we have 3 DIFFERENT Question categories in our getQuestions() method, we check for that.
     */
    @Test
    public void categoryListWithSorting(){
        analyser.afterRound(getSomeQuestions());

        HashMap<Categories, Double> percentageList = analyser.percentageList(false, true);

        assertEquals(3, percentageList.size());

    }

    /**
     * Here we created copies of the percentageList and changed their orders to descending and ascending.
     * We compared it to the result of the method.
     */
    @Test
    public void sortOrder(){
        analyser.afterRound(getSomeQuestions());

        HashMap<Categories, Double> percentageList = analyser.percentageList(false, true);

        HashMap<Categories, Double> expected = new HashMap<>();
        expected.put(Categories.BOOKS, 100d);
        expected.put(Categories.COMICS, 50d);
        expected.put(Categories.ART, 0d);

        assertEquals(expected, percentageList);

        percentageList = analyser.percentageList(false, false);

        HashMap<Categories, Double> expectedTwo = new HashMap<>();
        expectedTwo.put(Categories.ART, 0d);
        expectedTwo.put(Categories.COMICS, 50d);
        expectedTwo.put(Categories.BOOKS, 100d);

        assertEquals(expectedTwo, percentageList);
    }

    /**
     * Here we included all categories, also empty ones, giving us the size of 24.
     */
    @Test
    public void categoryListWithSorting2(){
        analyser.afterRound(getSomeQuestions());

        HashMap<Categories, Double> percentageList = analyser.percentageList(true, false);

        assertEquals(24, percentageList.size());
        assertEquals(100, percentageList.get(Categories.BOOKS), 0);
        assertEquals(50, percentageList.get(Categories.COMICS), 0);

    }


    /**
     * With this method, we simulate the call of an API.
     * @return the List containing all the Question. This is how it could look like after calling the questions from the API.
     */
    static List<Question> getSomeQuestions(){
        List<Question> qList = new ArrayList<>();
        Question q1 = new Question();
        Question q2 = new Question();
        Question q3 = new Question();
        Question q4 = new Question();

        qList.add(q1);
        qList.add(q2);
        qList.add(q3);
        qList.add(q4);

        fillQuestion(Categories.ART, Difficulties.EASY, Types.TRUEFALSE, false, q1);
        fillQuestion(Categories.BOOKS, Difficulties.MEDIUM, Types.TRUEFALSE, true, q2);
        fillQuestion(Categories.COMICS, Difficulties.HARD, Types.TRUEFALSE, true, q3);
        fillQuestion(Categories.COMICS, Difficulties.HARD, Types.MULTIPLECHOICE, false, q4);

        return qList;
    }

    /**
     * Simply puts the parameters into the given Question.
     * @param c chosen category.
     * @param d chosen difficulty.
     * @param t chosen type.
     * @param isCorrect necessary for the afterRound() method, because here the method works with attributes of the Question itself.
     * @param q
     */
    static void fillQuestion(Categories c, Difficulties d, Types t, boolean isCorrect, Question q){
        q.setCategory(c.getName());
        q.setDifficulty(d.getName());
        q.setType(t.getName());
        q.setCorrect(isCorrect);
    }

}