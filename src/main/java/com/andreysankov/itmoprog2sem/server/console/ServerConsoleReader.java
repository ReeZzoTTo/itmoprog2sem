package com.andreysankov.itmoprog2sem.server.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConsoleReader {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String readIfReady() throws IOException {
        if (reader.ready()) {
            return reader.readLine();
        }
        return null;
    }
}