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

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;

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

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                receiveBuffer.clear();
                SocketAddress responseAddress = channel.receive(receiveBuffer);

                if (responseAddress != null) {
                    receiveBuffer.flip();
                    byte[] responseBytes = new byte[receiveBuffer.remaining()];
                    receiveBuffer.get(responseBytes);

                    return deserialize(responseBytes);
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

    private Response deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);

        return (Response) objectStream.readObject();
    }
}
