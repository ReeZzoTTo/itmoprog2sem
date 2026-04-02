package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ClearCommand extends Command {
    public ClearCommand(Context context) {
        super(context);
        this.setName("clear");
        this.setDescription(" : очистить коллекцию");
    }

    @Override
    public Response execute(Request request) {
        getContext().getCollectionManager().clearCollection();

        return new Response(true, "Коллекция очищена");
    }
}
