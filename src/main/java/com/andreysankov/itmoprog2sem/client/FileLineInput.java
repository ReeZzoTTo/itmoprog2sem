package com.andreysankov.itmoprog2sem.client;

import java.io.BufferedReader;
import java.io.IOException;

import com.andreysankov.itmoprog2sem.common.util.LineInput;

public class FileLineInput implements LineInput {
    private final BufferedReader reader;

    public FileLineInput(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String readLine(String prompt) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}