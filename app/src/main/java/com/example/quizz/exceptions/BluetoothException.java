package com.example.quizz.exceptions;

import java.io.Serializable;

public class BluetoothException extends Exception implements Serializable {

    public BluetoothException(String message) {
        super(message);
    }
}


