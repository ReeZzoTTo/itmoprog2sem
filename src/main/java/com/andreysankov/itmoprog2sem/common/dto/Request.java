package com.andreysankov.itmoprog2sem.common.dto;

import java.io.Serializable;

import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CommandType commandType;
    private final String argument;
    private final LabWork labwork;

    public Request(
        CommandType commandType,
        String argumant,
        LabWork labwork
    ) {
        this.commandType = commandType;
        this.argument = argumant;
        this.labwork = labwork;
    }

    public CommandType getCommandType() { return this.commandType; }
    public String getArgument() { return this.argument; }
    public LabWork getLabWork() { return this.labwork; }

    @Override
    public String toString() {
        return "Request{" +
                "commandType=" + this.commandType +
                ", argument='" + this.argument + '\'' +
                ", labWork=" + this.labwork +
                '}';
    }
}