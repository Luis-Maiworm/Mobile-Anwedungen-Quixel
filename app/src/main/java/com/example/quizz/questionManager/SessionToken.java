package com.example.quizz.questionManager;

import java.io.Serializable;

/**
 * Stores the data of a getToken request. The attributes are necessary to store the provided data of the API call.
 */
public class SessionToken implements Serializable {

    public String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public int response_code;
    public int getResponse_code() {
        return response_code;
    }
    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public String response_message;
    public String getResponse_message() {
        return response_message;
    }
    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }
}
