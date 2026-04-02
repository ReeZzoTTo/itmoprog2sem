package com.andreysankov.itmoprog2sem.common.models;

import java.io.Serializable;

public class ArgumentId implements Serializable {
     private static final long serialVersionUID = 1L;
    private Long argumentId;
    private String responseMessage;


    public ArgumentId(String id) {
        if (id == null) {
            this.responseMessage = "Укажите ID элемента";
        }

        try {
            this.argumentId = Long.parseLong(id);
            if (this.argumentId <= 0) {
                this.responseMessage = "Аргумент должен быть строго больше 0";
                this.argumentId = 0L;
            }
        } catch (NumberFormatException e) {
            this.responseMessage = "Аргумент должен быть числом";
            this.argumentId = 0L;
        }
    }

    public String getResponseMessage() { return this.responseMessage; }
    public Long getId() { return this.argumentId; }
}
