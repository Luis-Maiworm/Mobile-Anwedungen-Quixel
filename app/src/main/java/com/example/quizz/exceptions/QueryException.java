package com.example.quizz.exceptions;

import java.io.Serializable;

public class QueryException extends Exception implements Serializable {

        public QueryException(String message) {
            super(message);
        }
    }


