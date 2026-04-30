package com.andreysankov.itmoprog2sem.client;

import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.models.ArgumentId;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class RequestBuilder {
    private final UserCredentials credentials;

    public RequestBuilder(UserCredentials credentials) {
        this.credentials = credentials;
    }

    public Request build(
        CommandType commandType,
        ArgumentId argument,
        LabWork labWork
    ) {
        return new Request(commandType, argument, labWork, this.credentials.getLogin(), this.credentials.getPassword());
    }
}
