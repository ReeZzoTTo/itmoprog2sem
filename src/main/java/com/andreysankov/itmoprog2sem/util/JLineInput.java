package com.andreysankov.itmoprog2sem.util;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

public class JLineInput implements LineInput {
    private final LineReader reader;

    public JLineInput(LineReader reader) {
        this.reader = reader;
    }

    public LineReader getLineReader() { return reader; }

    @Override
    public String readLine(String prompt) {
        try {
            return reader.readLine(prompt);
        } catch (UserInterruptException e) {
            return "exit";
        } catch (EndOfFileException e) {
            return "exit";
        }
    }
}
