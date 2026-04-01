package com.andreysankov.itmoprog2sem.common.exceptions;

public class AppException extends Exception {
    private boolean isFatalError = false;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, boolean isFatalError) {
        super(message);
        this.isFatalError = isFatalError;
    }

    public boolean getIsFatalError() {
        return this.isFatalError;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
