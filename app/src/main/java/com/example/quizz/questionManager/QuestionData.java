package com.example.quizz.questionManager;

import com.example.quizz.data.enums.Categories;

import java.io.Serializable;
import java.util.List;


/**
 * Includes a List<{@link Question}> to store all Questions of a request. This will be important for the API queries.
 * It also stores the List<{@link Categories}> to store all Categories. This will be important for our fix category list.
 */
public class QuestionData implements Serializable {

    private List<Question> results;
    private transient int response_code;

    public List<Question> getResults(){
        return this.results;
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

}
