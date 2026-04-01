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
        int pqmLength = 0;
        Double sumOfPQM = 0.0;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            Double pqm = element.getPersonalQualitiesMaximum();
            sumOfPQM += pqm;
            pqmLength += 1;
        }

        if (pqmLength == 0) {
            return new Response(false, "Коллекция пуста. Введите add чтобы добавить эелемент");
        }

        return new Response(true, "Среднее значение квалификации сотрудников : " + sumOfPQM / pqmLength);
    }
}
