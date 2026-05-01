package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class AverageOfPersonalQualititesMaximumCommand extends Command {
    public AverageOfPersonalQualititesMaximumCommand(Context context) {
        super(context);
        this.setName("average_of_personal_qualities_maximum");
        this.setDescription(" : вывести среднее значение поля personalQualitiesMaximum для всех элементов коллекции");
    }

    @Override
    public Response execute(Request request) {
        double average = getContext().getCollectionManager().getCollection().stream()
            .map(LabWork::getPersonalQualitiesMaximum)
            .filter(pqm -> pqm != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(Double.NaN);

        if (Double.isNaN(average)) {
            return new Response(false, "Нет элементов с указанным personalQualitiesMaximum");
        }

        return new Response(true, "Среднее значение квалификации сотрудников : " + average);
    }
}
