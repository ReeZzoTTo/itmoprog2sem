package com.andreysankov.itmoprog2sem.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andreysankov.itmoprog2sem.common.commands.*;
import com.andreysankov.itmoprog2sem.common.dto.*;
import com.andreysankov.itmoprog2sem.server.command.CommandProcessor;
import com.andreysankov.itmoprog2sem.server.console.*;
import com.andreysankov.itmoprog2sem.server.managers.*;
import com.andreysankov.itmoprog2sem.server.network.*;

// docker network create lab6-net
// docker run -it --rm --name lab6-server --network lab6-net -p 5555:5555/udp -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/data:/data" -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/logs:/app/logs" lab6-server
// docker run -it --rm --network lab6-net -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/data:/data" lab6-client lab6-server 5555

public class ServerApp {
    private static final int PORT = 5555;
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "data/collection.xml";

        logger.info("Запуск сервера");
        logger.info("Файл коллекции: {}", fileName);

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

        logger.info("Коллекция загружена. Размер коллекции: {}", collectionManager.getCollectionSize());

        ServerConsoleHandler consoleHandler = new ServerConsoleHandler(context);
        ServerConsoleReader consoleReader = new ServerConsoleReader();

        CommandProcessor processor = new CommandProcessor();

        registerCommands(context, commandManager, processor);
        logger.info("Команды зарегистрированы");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Получен сигнал завершения сервера. Сохраняем коллекцию");
            String aboutError = SaveCommand.save(context);

            if (aboutError == null) {
                logger.info("Коллекция успешно сохранена при завершении сервера");
            } else {
                logger.error("Ошибка сохранения при завершении сервера: {}", aboutError);
            }
        }));

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            socket.setSoTimeout(200);
            RequestReceiver receiver = new RequestReceiver(socket);
            RequestReader reader = new RequestReader();
            ResponseSender sender = new ResponseSender(socket);

            logger.info("Сервер запущен на порту {}", PORT);

            while (true) {
                String line = consoleReader.readIfReady();
                if (line != null) {
                    consoleHandler.handle(line);
                }

                try {
                    DatagramPacket packet = receiver.receive();

                    Response response;

                    try {
                        Request request = reader.read(packet.getData(), packet.getLength());
                        
                        logger.info(
                            "Получен запрос от {}:{} | команда={} | размер={} байт",
                            packet.getAddress().getHostAddress(),
                            packet.getPort(),
                            request.getCommandType(),
                            packet.getLength()
                        );
                        
                        commandManager.addToHistory(request.getCommandType().getName());
                        response = processor.process(request);

                        logger.info(
                            "Команда {} обработана. Успех={} ",
                            request.getCommandType(),
                            response.isSuccess()
                        );

                    } catch (Exception e) {
                        logger.error(
                            "Ошибка обработки запроса от {}:{}",
                            packet.getAddress().getHostAddress(),
                            packet.getPort(),
                            e
                        );
                        response = new Response(false, "ошибка обработки запроса: " + e.getMessage());
                    }

                    sender.send(response, packet.getAddress(), packet.getPort());

                    logger.info(
                        "Ответ отправлен клиенту {}:{} | успех={}",
                        packet.getAddress().getHostAddress(),
                        packet.getPort(),
                        response.isSuccess()
                    );
                } catch (SocketTimeoutException e) {}
            } 
        } catch (IOException e) {
            logger.error("Ошибка запуска сервера", e);
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
