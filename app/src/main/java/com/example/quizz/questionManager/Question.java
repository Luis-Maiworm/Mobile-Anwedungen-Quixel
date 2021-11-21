package com.example.quizz.questionManager;

import com.example.quizz.data.enums.Categories;
import com.example.quizz.data.enums.Difficulties;
import com.example.quizz.data.enums.Types;

import java.io.Serializable;
import java.util.List;

/**
 * Die Klasse {@code Question} beinhaltet alle Informationen die man für eine Frage benötigt. Sie werden
 * direkt aus der json Anfrage übernommen.
 */
public class Question implements Serializable {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public transient boolean isCorrect; //todo getter and setter FALLS wirklich so geplant

    public String getCategoryString(){
        return this.category;
    }
    public String getTypeString(){
        return this.type;
    }
    public String getDifficultyString(){
        return this.difficulty;
    }

    public Categories getCategory() {
        for(Categories c : Categories.values()){
            if(c.getName().equals(this.category)){
                return c;
            }
        }
        return null;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Types getType() {
        for(Types t : Types.values()){
            if(t.getName().equals(this.type)){
                return t;
            }
        }
        return null;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Difficulties getDifficulty() {
        for(Difficulties d : Difficulties.values()){
            if(d.getName().equals(this.difficulty)){
                return d;
            }
        }
        return null;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }
    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }
    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }


}
