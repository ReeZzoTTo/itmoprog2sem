package com.andreysankov.itmoprog2sem.server.network;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.dto.ResponseChunk;
import com.andreysankov.itmoprog2sem.common.util.SerializationUtils;

public class ResponseSender {
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
    private final DatagramSocket socket;

    public ResponseSender(DatagramSocket socket) {
        this.socket = socket;
    }

    public void send(
        Response response,
        InetAddress address,
        int port
    ) throws IOException {
        byte[] responseBytes = SerializationUtils.serialize(response);
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, address, port);
        socket.send(packet);

        logger.debug(
            "Ответ отправлен на {}:{} размером {} байт",
            address.getHostAddress(),
            port,
            responseBytes.length
        );
    }

    public void sendChunks(List<ResponseChunk> responseChunks, InetAddress address, int port) 
    throws IOException {
        for (ResponseChunk chunk : responseChunks) {
            byte[] responseChunkBytes = SerializationUtils.serialize(chunk);
            DatagramPacket packet = new DatagramPacket(responseChunkBytes, responseChunkBytes.length, address, port);

            socket.send(packet);

            logger.debug(
                 "Отправлена часть ответа {}/{} клиенту {}:{}",
                chunk.getPartIndex() + 1,
                chunk.getTotalParts(),
                address.getHostAddress(),
                port
            );
        }
        
    }
}
