package com.andreysankov.itmoprog2sem.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.andreysankov.itmoprog2sem.common.util.LineInput;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.util.JLineInput;

// java -cp .\target\itmoprog2sem-1.0-Lab6.jar com.andreysankov.itmoprog2sem.client.ClientApp
// java -jar target\itmoprog2sem-1.0-Lab6.jar data/data.xml

public class ClientApp {
    public static void main(String[] args) {
        
        LineReader reader = null;
        Terminal terminal;
        DefaultHistory history = null;
        try {   
            history = new DefaultHistory();
            terminal = TerminalBuilder.builder().system(true).provider("jni").encoding(StandardCharsets.UTF_8).build();
            reader = LineReaderBuilder.builder().terminal(terminal).history(history).build();
            history.load();
        } catch (IOException e) {
            System.out.println("Возникла ошибка : ");
            e.printStackTrace();
            System.exit(1);
        }

        JLineInput console = new JLineInput(reader);

        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5555;

        LabWorkInputReader labWorkInputReader = new LabWorkInputReader((LineInput)console);
        ClientCommandParser clientCommandParser = new ClientCommandParser();
        Client client = new Client(host, port, 3000);
        ScriptExecutor scriptExecutor = new ScriptExecutor(client, clientCommandParser);

        clientCommandParser.setLWIReader(labWorkInputReader);

        while (true) {
            String input = console.readLine("> ");
            if (input == null) continue;

            input = input.trim();
            if (input.isEmpty()) continue;

            String[] inputSplit = input.split("\\s+");
            String commandName = inputSplit[0].toLowerCase();

            if ("execute_script".equals(commandName)) {
                if (inputSplit.length < 2) {
                    System.out.println("Команда execute_script требует имя файла.");
                    continue;
                }

                scriptExecutor.executeScript(inputSplit[1]);
                continue;
            }

            CommandType commandType = clientCommandParser.getCommand(commandName);

            if (commandType == CommandType.EXIT) {
                System.out.println("Завершение клиента");
                break;
            }

            if (commandType != null) {
                Request request = clientCommandParser.parseCommand(commandType, inputSplit);
                if (request != null) {
                    Response response = client.sendRequest(request);
                    System.out.println(response.getMessage());
                }
            } else {
                System.out.println("Неизвестная команда : Введите help");
            }
        }
    }
}
