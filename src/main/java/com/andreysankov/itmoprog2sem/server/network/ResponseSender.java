package com.andreysankov.itmoprog2sem.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.util.SerializationUtils;

public class ResponseSender {
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
    }
}
