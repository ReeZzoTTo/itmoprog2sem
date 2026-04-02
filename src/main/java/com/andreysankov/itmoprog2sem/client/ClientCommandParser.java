package com.andreysankov.itmoprog2sem.client;

import java.util.HashMap;
import java.util.Map;

import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.models.ArgumentId;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class ClientCommandParser {
    private Map<String, CommandType> commands = new HashMap<>();
    private LabWorkInputReader labWorkInputReader;
    private RequestBuilder requestBuilder = new RequestBuilder();

    public ClientCommandParser() {
        for (CommandType value : CommandType.values()) {
            this.commands.put(value.getName(), value);
        }
    }

    public CommandType getCommand(String key) {
        return this.commands.get(key.toLowerCase());
    }

    public void setLWIReader(LabWorkInputReader LWIReader) {
        this.labWorkInputReader = LWIReader;
    }

    public Request parseCommand(CommandType command, String[] userInput) {
        ArgumentId argument = null;
        LabWork labwork = null;

        if (command.isRequiredArgument()) {
            if (userInput.length < 2) {
                System.out.println("Команда " + command.getName() + " требует аргумент (id)");
                return null;
            }
            argument = new ArgumentId(userInput[1]);

            if (argument.getId() == 0) {
                System.out.println(argument.getResponseMessage());
                return null;
            }
        }

        if (command.isRequiredLabwork()) {
            System.out.println("Объявление объекта");
            labwork = this.labWorkInputReader.getLabWork();
        }

        return this.requestBuilder.build(command, argument, labwork);
    }
}
