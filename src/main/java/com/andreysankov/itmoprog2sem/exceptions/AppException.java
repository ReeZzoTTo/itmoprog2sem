package com.andreysankov.itmoprog2sem.exceptions;

public class AppException extends Exception {
    public AppException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
