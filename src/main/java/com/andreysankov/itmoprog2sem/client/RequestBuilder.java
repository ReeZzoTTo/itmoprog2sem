package com.andreysankov.itmoprog2sem.client;

import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class RequestBuilder {
    public Request build(
        CommandType commandType,
        String argument,
        LabWork labWork
    ) {
        return new Request(commandType, argument, labWork);
    }
}
