package com.example.quizz.data.enums;

/**
 *  Enum storing all available difficulties:
 *  <p>EASY = Easiest</p>
 *  <p>MEDIUM = Medium</p>
 *  <p>hard = Hardest</p>
 */
public enum Difficulties {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String name;


    Difficulties(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
