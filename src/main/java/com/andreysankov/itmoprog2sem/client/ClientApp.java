package com.andreysankov.itmoprog2sem.client;

import java.io.IOException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.andreysankov.itmoprog2sem.client.utils.LineInput;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.util.JLineInput;

public class ClientApp {
    public static void main(String[] args) {
        
        LineReader reader = null;
        Terminal terminal;
        DefaultHistory history = null;
        try {   
            history = new DefaultHistory();
            terminal = TerminalBuilder.builder().system(true).build();
            reader = LineReaderBuilder.builder().terminal(terminal).history(history).build();
            // reader.setVariable(LineReader.HISTORY_FILE, Paths.get("/data/.labwork_history"));
            history.load();
        } catch (IOException e) {
            System.out.println("Возникла ошибка : ");
            e.printStackTrace();
            System.exit(1);
        }

        JLineInput console = new JLineInput(reader);

        LabWorkInputReader labWorkInputReader = new LabWorkInputReader((LineInput)console);
        ClientCommandParser clientCommandParser = new ClientCommandParser();
        Client client = new Client("localhost", 5555, 3000);

        clientCommandParser.setLWIReader(labWorkInputReader);

        while (true) {
            String input = console.readLine("> ");
            if (input == null) continue;

            input = input.trim();
            if (input.isEmpty()) continue;

            String[] inputSplit = input.split("\\s+");
            
            CommandType commandType = clientCommandParser.getCommand(inputSplit[0]);

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
