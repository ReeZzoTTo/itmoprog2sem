package com.andreysankov.itmoprog2sem.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.Date;

import com.andreysankov.itmoprog2sem.common.commands.*;
import com.andreysankov.itmoprog2sem.common.dto.*;
import com.andreysankov.itmoprog2sem.server.command.CommandProcessor;
import com.andreysankov.itmoprog2sem.server.managers.*;
import com.andreysankov.itmoprog2sem.server.network.*;

public class ServerApp {
    private static final int PORT = 5555;

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "data/collection.xml";

        FileManager fileManager = new FileManager(fileName);
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();

        Context context = new Context(
            fileManager, 
            commandManager, 
            collectionManager
        );

        collectionManager.setCollection(fileManager.readFile());
        collectionManager.setInitializationDate(context);
        collectionManager.setDisciplineMap();

        CommandProcessor processor = new CommandProcessor();

        registerCommands(context, commandManager, processor);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                collectionManager.sortCollection();
                fileManager.saveFile(collectionManager.getCollection());
                System.out.println("Коллекция сохраненеа при завершении сервера");
            } catch (IOException e) {
                System.out.println("Ошибка сохранения при завершении: " + e.getMessage());
            }
        }));

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            RequestReceiver receiver = new RequestReceiver(socket);
            RequestReader reader = new RequestReader();
            ResponseSender sender = new ResponseSender(socket);

            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                DatagramPacket packet = receiver.receive();

                Response response;

                try {
                    Request request = reader.read(packet.getData(), packet.getLength());
                    
                    System.out.println("[" + new Date() + "] Получен запрос: " + request);

                    commandManager.addToHistory(request.getCommandType().getName());
                    response = processor.process(request);
                } catch (Exception e) {
                    response = new Response(false, "ошибка обработки запроса: " + e.getMessage());
                }

                sender.send(response, packet.getAddress(), packet.getPort());
                System.out.println("[" + new Date() + "] Ответ отправлен: " + response);
            } 
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }

    private static void registerCommands(
        Context context,
        CommandManager commandManager,
        CommandProcessor processor
    ) {
        AddCommand add                  = new AddCommand(context);
        AddIfMinCommand addIfMin        = new AddIfMinCommand(context);
        ClearCommand clear              = new ClearCommand(context);
        HelpCommand help                = new HelpCommand(context);
        HistoryCommand history          = new HistoryCommand(context);
        InfoCommand info                = new InfoCommand(context);
        RemoveByIdCommand removeById    = new RemoveByIdCommand(context);
        RemoveLowerCommand removeLower  = new RemoveLowerCommand(context);
        ShowCommand show                = new ShowCommand(context);
        UpdateIdCommand updateId        = new UpdateIdCommand(context);
        PrintFieldDescendingDisciplineCommand printDiscipline = new PrintFieldDescendingDisciplineCommand(context);
        GroupCountingByCreationDateCommand group              = new GroupCountingByCreationDateCommand(context);
        AverageOfPersonalQualititesMaximumCommand average     = new AverageOfPersonalQualititesMaximumCommand(context);
                
        commandManager.registerCommand("add", add);
        commandManager.registerCommand("add_if_min", addIfMin);
        commandManager.registerCommand("average_of_personal_qualities_maximum", average);
        commandManager.registerCommand("clear", clear);
        commandManager.registerCommand("group_counting_by_creation_date", group);
        commandManager.registerCommand("help", help);
        commandManager.registerCommand("history", history);
        commandManager.registerCommand("info", info);
        commandManager.registerCommand("print_field_descending_discipline", printDiscipline);
        commandManager.registerCommand("remove_by_id", removeById);
        commandManager.registerCommand("remove_lower", removeLower);
        commandManager.registerCommand("show", show);
        commandManager.registerCommand("update_id", updateId);

        processor.register(CommandType.ADD, add);
        processor.register(CommandType.ADD_IF_MIN, addIfMin);
        processor.register(CommandType.AVERAGE_OF_PERSONAL_QUALITIES_MAXIMUM, average);
        processor.register(CommandType.CLEAR, clear);
        processor.register(CommandType.GROUP_COUNTING_BY_CREATION_DATE, group);
        processor.register(CommandType.HELP, help);
        processor.register(CommandType.HISTORY, history);
        processor.register(CommandType.INFO, info);
        processor.register(CommandType.PRINT_FIELD_DESCENDING_DISCIPLINE, printDiscipline);
        processor.register(CommandType.REMOVE_BY_ID, removeById);
        processor.register(CommandType.REMOVE_LOWER, removeLower);
        processor.register(CommandType.SHOW, show);
        processor.register(CommandType.UPDATE_ID, updateId);
    }
}
