package com.example.quizz.questionManager;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable {

    public List<Question> results;
    public transient int response_code;
    public List<Category> getTrivia_categories() {
        return trivia_categories;
    }


    public List<Question> getResults() {
        return results;
    }
    public void setResults(List<Question> results) {
        this.results = results;
    }

    public int getResponse_code() {
        return response_code;
    }
    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public void setTrivia_categories(List<Category> trivia_categories) {
        this.trivia_categories = trivia_categories;
    }
    public List<Category> trivia_categories;
}
