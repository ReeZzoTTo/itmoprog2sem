package com.andreysankov.itmoprog2sem.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.Map;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.dto.ResponseChunk;

public class Client {
    private final String host;
    private final int port;
    private final int timeoutMillis;

    public Client(String host, int port, int timeoutMillis) {
        this.host = host;
        this.port = port;
        this.timeoutMillis = timeoutMillis;
    }

    public Response sendRequest(Request request) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.configureBlocking(false);

            InetSocketAddress serverAddress = new InetSocketAddress(host, port);

            byte[] requestBytes = serialize(request);
            ByteBuffer sendBuffer = ByteBuffer.wrap(requestBytes);
            try {
                channel.send(sendBuffer, serverAddress);
            } catch (UnresolvedAddressException e) {
                return new Response(false, "Сервер временно недоступен или не ответил вовремя");
            }

            ByteBuffer receiveBuffer = ByteBuffer.allocate(65535);
            long startTime = System.currentTimeMillis();

            Map<Integer, String> parts = new HashMap<>();
            String requestId = null;
            int totalParts = -1;
            boolean succes = false;

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                receiveBuffer.clear();
                SocketAddress responseAddress = channel.receive(receiveBuffer);

                if (responseAddress != null) {
                    receiveBuffer.flip();
                    byte[] responseBytes = new byte[receiveBuffer.remaining()];
                    receiveBuffer.get(responseBytes);

                    Object obj = deserialize(responseBytes);

                    if (obj instanceof ResponseChunk chunk) {
                        if (requestId == null) {
                            requestId = chunk.getRequestId();
                            totalParts = chunk.getTotalParts();
                            succes = chunk.isSuccess();
                        }

                        if (chunk.getRequestId().equals(requestId)) {
                            parts.put(chunk.getPartIndex(), chunk.getPayload());

                            if (parts.size() == totalParts) {
                                StringBuilder fullMessage = new StringBuilder();

                                for (int i = 0; i < totalParts; i++) {
                                    fullMessage.append(parts.getOrDefault(i, ""));
                                }

                                return new Response(succes, fullMessage.toString());
                            }
                        }
                    } else if (obj instanceof Response response) {
                        return response;
                    } else {
                        return new Response(false, "Получен объект неизвестного типа");
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new Response(false, "Ожидание ответа было прервано.");
                }
            }

            return new Response(false, "Сервер временно недоступен или не ответил вовремя");
        } catch (IOException | ClassNotFoundException e) {
            return new Response(false, "Ошибка обмена данными с сервером: " + e.getMessage());
        }
    }

    private byte[] serialize(Request request) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(request);
        objectStream.flush();
        return byteStream.toByteArray();
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);

        return objectStream.readObject();
    }
}
