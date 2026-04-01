package com.andreysankov.itmoprog2sem.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RequestReceiver {
    private static final int BUFFER_SIZE = 65535;
    private final DatagramSocket socket;

    public RequestReceiver(DatagramSocket socket) {
        this.socket = socket;
    }

    public DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        return packet;
    }
}
