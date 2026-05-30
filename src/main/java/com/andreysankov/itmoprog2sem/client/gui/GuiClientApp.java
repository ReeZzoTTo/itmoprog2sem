package com.andreysankov.itmoprog2sem.client.gui;

import javax.swing.SwingUtilities;

public class GuiClientApp {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5555;

        SwingUtilities.invokeLater(() -> {
            AuthFrame authFrame = new AuthFrame(host, port);
            authFrame.setVisible(true);
        });
    }
}
