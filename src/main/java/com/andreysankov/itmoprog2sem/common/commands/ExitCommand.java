package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ExitCommand extends Command {
    public ExitCommand(Context context) {
        super(context);
        this.setName("exit");
        this.setDescription(" : завершить программу (без сохранения в файл)");
    }

    @Override
    public Response execute(Request request) {
        return null;
    }
}
