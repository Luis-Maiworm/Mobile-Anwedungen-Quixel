package com.example.quizz.data.gameData;

/**
 * Enum storing all available types:
 * <p>multiple = Multiple Choice Questions</p>
 * <p>boolean = True or False Questions</p>
 */
public enum Types {
    MULTIPLECHOICE("multiple", "Multiple Choice"),
    TRUEFALSE("boolean", "True / False");


    private final String name;
    private final String realName;

    Types(String name, String realName) {
        this.name = name;
        this.realName = realName;
    }

    public String getName() {
        return this.name;
    }

    public String getRealName() {
        return realName;
    }
}
