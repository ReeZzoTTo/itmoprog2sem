package com.andreysankov.itmoprog2sem.managers;

public class ErrorManager {
    private Exception error;
    private boolean isFatalError = false;

    public void setException(Exception e) {
        this.error = e;
    }

    public void setException(Exception e, boolean fatal) {
        this.error = e;
        this.isFatalError = fatal;
    }

    public void executeError() {
        System.out.println("Возникла " + (isFatalError ? "фатальная" : "") + " ошибка : " + this.error.getMessage());

        if (isFatalError) System.exit(1);
    }

}
