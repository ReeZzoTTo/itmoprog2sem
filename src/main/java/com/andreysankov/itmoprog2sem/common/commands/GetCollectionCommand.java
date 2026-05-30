package com.andreysankov.itmoprog2sem.common.commands;

import java.util.ArrayList;
import java.util.List;

import com.andreysankov.itmoprog2sem.common.dto.CollectionResponse;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class GetCollectionCommand extends Command {
    public GetCollectionCommand(Context context) {
        super(context);
        this.setName("get_collection");
        this.setDescription(" : получить коллекцию для GUI");
    }

    @Override
    public Response execute(Request request) {
        List<LabWork> collection = new ArrayList<>(
                getContext().getCollectionManager().getCollection()
        );

        return new CollectionResponse(
                true,
                "Коллекция получена",
                collection
        );
    }
}
