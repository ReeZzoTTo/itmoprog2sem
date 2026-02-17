package com.andreysankov.itmoprog2sem.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand(Context context) {
        super(context);
        this.setName("execute_script");
        this.setDescription(" file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }
    
    @Override
    public boolean execute() {
        String scriptFileName;
        File file;

        try {
            scriptFileName = getContext().getCommandManager().getArguments()[1];
            file = new File(scriptFileName);

            if (!file.exists()) {
                System.out.println("Файл не существует");
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Укажите путь к файлу");
            return true;
        }

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            Scanner scanner = new Scanner(bis, StandardCharsets.UTF_8);
        
            getContext().getInputManager().readConsole(scanner);
        } catch (IOException e) {
            System.out.println("Ошибка исполнения файла " + e.getMessage());
        } 

        return true;
    }
}
