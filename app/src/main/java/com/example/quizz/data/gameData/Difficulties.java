package com.example.quizz.data.gameData;

/**
 * Enum storing all available difficulties:
 * <p>EASY = Easiest</p>
 * <p>MEDIUM = Medium</p>
 * <p>hard = Hardest</p>
 */
public enum Difficulties {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private final String name;


    Difficulties(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
