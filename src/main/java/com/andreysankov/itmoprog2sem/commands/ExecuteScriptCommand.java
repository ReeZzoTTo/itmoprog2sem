package com.andreysankov.itmoprog2sem.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
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
                getContext().getErrorManager().setException(new AppException("Файл не существует"));
                return false;
            }
            if (getContext().getInputManager().getFileScriptsSet().contains(scriptFileName)) {
                getContext().getErrorManager().setException(new AppException("Обнаружена рекурсия. Выполнение файла прервано"), true);
                return false;
            }
            getContext().getInputManager().addFileScriptToSet(scriptFileName);
        } catch (ArrayIndexOutOfBoundsException e) {
            getContext().getErrorManager().setException(new AppException("Укажите путь к файлу"));
            return false;
        }

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            Scanner scanner = new Scanner(bis, StandardCharsets.UTF_8);
            Scanner oldScanner = getContext().getScanner();

            getContext().setScanner(scanner);
            getContext().getInputManager().readConsole(scanner);
            getContext().setScanner(oldScanner);
        } catch (IOException e) {
            getContext().getErrorManager().setException(new AppException("Ошибка исполнения файла " + e.getMessage()));
            return false;
        } finally {
            getContext().getInputManager().getFileScriptsSet().remove(scriptFileName);
        }

        return true;
    }
}
