package com.andreysankov.itmoprog2sem.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;

public class ScriptExecutor {
    private final Client client;
    private final ClientCommandParser parser;
    private final Set<String> activeScripts = new HashSet<>();

    public ScriptExecutor(Client client, ClientCommandParser parser) {
        this.client = client;
        this.parser = parser;
    }

    public void executeScript(String fileName) {
        File file = new File(fileName);
        String absolutePath = file.getAbsolutePath();

        if (activeScripts.contains(absolutePath)) {
            System.out.println("Обнаружена рекурсия: скрипт " + fileName + " уже выполняется.");
            return;
        }

        if (!file.exists() || !file.isFile()) {
            System.out.println("Файл скрипта не найден: " + fileName);
            return;
        }

        activeScripts.add(absolutePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            FileLineInput fileLineInput = new FileLineInput(reader);
            LabWorkInputReader scriptLabWorkReader = new LabWorkInputReader(fileLineInput);

            LabWorkInputReader oldReader = parser.getLabWorkInputReader();
            parser.setLWIReader(scriptLabWorkReader);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] inputSplit = line.split("\\s+");
                String commandName = inputSplit[0].toLowerCase();

                if ("execute_script".equals(commandName)) {
                    if (inputSplit.length < 2) {
                        System.out.println("Команда execute_script требует имя файла.");
                        continue;
                    }

                    executeScript(inputSplit[1]);
                    continue;
                }

                if ("exit".equals(commandName)) {
                    System.out.println("Команда exit внутри скрипта игнорируется.");
                    continue;
                }

                CommandType commandType = parser.getCommand(commandName);

                if (commandType == null) {
                    System.out.println("Неизвестная команда в скрипте: " + commandName);
                    continue;
                }

                Request request = parser.parseCommand(commandType, inputSplit);
                if (request != null) {
                    Response response = client.sendRequest(request);
                    System.out.println(response.getMessage());
                }
            }

            parser.setLWIReader(oldReader);

        } catch (IOException e) {
            System.out.println("Ошибка чтения скрипта: " + e.getMessage());
        } finally {
            activeScripts.remove(absolutePath);
        }
    }
}