package com.example.quizz.data.playerData;

import static org.junit.Assert.*;
import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.Types;

import org.junit.Before;
import org.junit.Test;

public class StatisticsTest {

    static Statistics stats;

    @Before
    public void init(){
        stats = new Statistics();
    }


    @Test
    public void incrementTotal(){
        stats.incrementTotal(true);

        assertEquals(1, stats.getTotalRightAnswers());
        assertEquals(0, stats.getTotalWrongAnswers());
        assertEquals(1, stats.getTotalAnswers());
    }

    @Test
    public void incrementRightAndWrongTotal(){
        stats.incrementTotal(true);
        stats.incrementTotal(true);
        stats.incrementTotal(false);

        assertEquals(2, stats.getTotalRightAnswers());
        assertEquals(1, stats.getTotalWrongAnswers());
    }

    @Test
    public void incrementByCategory(){
        stats.incrementAnswerPerCategory(Categories.ART, true);

        stats.incrementAnswerPerCategory(Categories.ANIMALS, false);
        stats.incrementAnswerPerCategory(Categories.ANIMALS, false);

        assertEquals(1, stats.getAnswerPerCategory(Categories.ART, true));
        assertEquals(2, stats.getAnswerPerCategory(Categories.ANIMALS, false));
    }

    @Test
    public void incrementByType(){
        stats.incrementAnswerPerType(Types.MULTIPLECHOICE, false);

        assertEquals(1, stats.getAnswerPerType(Types.MULTIPLECHOICE, false));
    }

    @Test
    public void incrementByDifficulty(){
        stats.incrementAnswerPerDifficulty(Difficulties.HARD, true);
        stats.incrementAnswerPerDifficulty(Difficulties.HARD, false);

        assertEquals(1, stats.getAnswerPerDifficulty(Difficulties.HARD, true));
        assertEquals(1, stats.getAnswerPerDifficulty(Difficulties.HARD, false));
    }

}