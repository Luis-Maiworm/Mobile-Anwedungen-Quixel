package com.example.quizz.data.enums;

/**
 *  Enum storing all available types:
 *  <p>multiple = Multiple Choice Questions</p>
 *  <p>boolean = True or False Questions</p>
 */
public enum Types {
    MULTIPLECHOICE("multiple"),
    TRUEFALSE("boolean");

    private final String name;


    Types(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
