package com.andreysankov.itmoprog2sem;

import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        String filename = args[0];
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 5; i++) {
            String input = scanner.nextLine();
            System.out.println(input + " --> " + filename);
        }

        scanner.close();
    }
}

