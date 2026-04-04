package com.andreysankov.itmoprog2sem.server.network;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RequestReceiver {
    private static final Logger logger = LoggerFactory.getLogger(RequestReceiver.class);
    private static final int BUFFER_SIZE = 65535;
    private final DatagramSocket socket;

    public RequestReceiver(DatagramSocket socket) {
        this.socket = socket;
    }

    public DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        logger.debug(
            "Получен UDP-пакет от {}:{} размером {} байт",
            packet.getAddress().getHostAddress(),
            packet.getPort(),
            packet.getLength()
        );

        return packet;
    }
}
