package com.andreysankov.itmoprog2sem.common.dto;

import java.io.Serializable;

import com.andreysankov.itmoprog2sem.common.models.ArgumentId;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CommandType commandType;
    private final ArgumentId argument;
    private final LabWork labwork;

    public Request(
        CommandType commandType,
        ArgumentId argument,
        LabWork labwork
    ) {
        this.commandType = commandType;
        this.argument = argument;
        this.labwork = labwork;
    }

    public CommandType getCommandType() { return this.commandType; }
    public ArgumentId getArgument() { return this.argument; }
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