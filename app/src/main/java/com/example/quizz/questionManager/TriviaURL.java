package com.example.quizz.questionManager;

public enum TriviaURL {

    /**
     * First part of the Question call URL.
     */
    BASICURL("https://opentdb.com/api.php?amount="),
    /**
     * Get a new Token.
     */                                           //todo -||-
    NEWTOKEN("https://opentdb.com/api_token.php?command=request"),
    /**
     * To reset a token.
     * Append the token which should be reset.
     */
    RESETTOKEN("https://opentdb.com/api_token.php?command=reset&token="),
    /**
     * All categories with corresponding id's.
     */
    CATEGORYLOOKUP("https://opentdb.com/api_category.php"),
    /**
     * Concat the wanted Category ID to call the data.
     */
    CATEGORYQUESTIONCOUNTPERCAT("https://opentdb.com/api_count.php?category="),
    /**
     * All Question numbers, including unverified and pending.
     */
    CATEGORYQUESTIONCOUNTTOTAL("https://opentdb.com/api_count_global.php");

    private final String link;

    TriviaURL(String name){
        this.link = name;
    }

    public String getLink(){
        return this.link;
    }
}
