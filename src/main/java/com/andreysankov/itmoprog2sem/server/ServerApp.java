package com.andreysankov.itmoprog2sem.server;

// docker compose build

// docker compose up server
// docker compose up postgres 
// docker compose run --rm client

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andreysankov.itmoprog2sem.common.commands.*;
import com.andreysankov.itmoprog2sem.common.dto.*;
import com.andreysankov.itmoprog2sem.server.command.CommandProcessor;
import com.andreysankov.itmoprog2sem.server.console.*;
import com.andreysankov.itmoprog2sem.server.database.DatabaseManager;
import com.andreysankov.itmoprog2sem.server.managers.*;
import com.andreysankov.itmoprog2sem.server.network.*;

// docker build -t lab6-server .
// docker build -t lab6-client -f Dockerfile.client

// docker network create lab6-net
// docker run -it --rm --name lab6-server --network lab6-net -p 5555:5555/udp -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/data:/data" -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/logs:/app/logs" lab6-server
// docker run -it --rm --network lab6-net -v "C:/Users/dioma/ITp/GitHubProjects/itmoprog2sem/data:/data" lab6-client lab6-server 5555

public class ServerApp {
    private static final int PORT = 5555;
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
    public static void main(String[] args) {
        logger.info("Запуск сервера");
        
        DatabaseManager databaseManager = new DatabaseManager();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();

        try {
            databaseManager.initialize();
            System.out.println("База данных успешно инициализирована");
        } catch (SQLException e) {
            System.err.println("При инициализации базы данных произошла ошибка: " + e.getMessage());
            return;
        }

        Context context = new Context( 
            commandManager, 
            collectionManager,
            databaseManager
        );

        try {
            collectionManager.setCollection(context.getLabWorkRepository().loadCollection());
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке коллекции в память: " + e.getMessage());
            System.exit(1);
        }
        collectionManager.setInitializationDate();
        collectionManager.setDisciplineMap();

        logger.info("Коллекция загружена. Размер коллекции: {}", collectionManager.getCollectionSize());

        ServerConsoleHandler consoleHandler = new ServerConsoleHandler(context);
        ServerConsoleReader consoleReader = new ServerConsoleReader();

        CommandProcessor processor = new CommandProcessor(context);

        registerCommands(context, commandManager, processor);
        logger.info("Команды зарегистрированы");

        ExecutorService readPool = Executors.newFixedThreadPool(4);
        ExecutorService processPool = Executors.newFixedThreadPool(8);
        ExecutorService sendPool = Executors.newCachedThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Получен сигнал завершения сервера. Завершаем пул потоков");

            readPool.shutdownNow();
            processPool.shutdownNow();
            sendPool.shutdownNow();
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

                    byte[] packetData = packet.getData();
                    int packetLength = packet.getLength();
                    InetAddress clientAddress = packet.getAddress();
                    int clientPort = packet.getPort();

                    readPool.submit(() -> {
                        try {
                            Request request = reader.read(packetData, packetLength);

                            logger.info(
                                "Получен запрос от {}:{} | команда={} | размер={} байт",
                                clientAddress.getHostAddress(),
                                clientPort,
                                request.getCommandType(),
                                packetLength
                            );

                            processPool.submit(() -> {
                                Response response;

                                try {
                                    commandManager.addToHistory(request.getCommandType().getName());
                                    response = processor.process(request);

                                    logger.info(
                                        "Команда {} обработана. Успех={}",
                                        request.getCommandType(),
                                        response.isSuccess()
                                    );

                                } catch (Exception e) {
                                    logger.error(
                                        "Ошибка обработки команды от {}:{}",
                                        clientAddress.getHostAddress(),
                                        clientPort,
                                        e
                                    );
                                    response = new Response(false, "Ошибка обработки команды: " + e.getMessage());
                                }

                                Response finalResponse = response;

                                sendPool.submit(() -> {
                                    try {
                                        List<ResponseChunk> chunks = ResponseChunker.split(finalResponse);

                                        sender.sendChunks(chunks, clientAddress, clientPort);

                                        logger.info(
                                            "Ответ отправлен клиенту {}:{} | успех={}",
                                            clientAddress.getHostAddress(),
                                            clientPort,
                                            finalResponse.isSuccess()
                                        );

                                    } catch (Exception e) {
                                        logger.error(
                                            "Ошибка отправки ответа клиенту {}:{}",
                                            clientAddress.getHostAddress(),
                                            clientPort,
                                            e
                                        );
                                    }
                                });
                            });

                        } catch (Exception e) {
                            logger.error(
                                "Ошибка чтения запроса от {}:{}",
                                clientAddress.getHostAddress(),
                                clientPort,
                                e
                            );

                            Response response = new Response(false, "Ошибка чтения запроса: " + e.getMessage());

                            sendPool.submit(() -> {
                                try {
                                    List<ResponseChunk> chunks = ResponseChunker.split(response);
                                    sender.sendChunks(chunks, clientAddress, clientPort);
                                } catch (Exception sendException) {
                                    logger.error(
                                        "Ошибка отправки сообщения об ошибке клиенту {}:{}",
                                        clientAddress.getHostAddress(),
                                        clientPort,
                                        sendException
                                    );
                                }
                            });
                        }
                    });   
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
        RegisterCommand register = new RegisterCommand(context);
        LoginCommand login       = new LoginCommand(context);
        
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
        commandManager.registerCommand("register", register);
        commandManager.registerCommand("login", login);

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
        processor.register(CommandType.REGISTER, register);
        processor.register(CommandType.LOGIN, login);
    }
}
