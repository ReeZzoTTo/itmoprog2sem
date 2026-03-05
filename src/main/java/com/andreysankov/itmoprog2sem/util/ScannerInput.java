package com.andreysankov.itmoprog2sem.util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScannerInput implements LineInput {
    private final Scanner scanner;

    public ScannerInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String readLine(String prompt) {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
