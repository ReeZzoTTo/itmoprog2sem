package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class AverageOfPersonalQualititesMaximumCommand extends AbstractCommand {
    public AverageOfPersonalQualititesMaximumCommand(Context context) {
        super(context);
        this.setName("average_of_personal_qualities_maximum");
        this.setDescription(" : вывести среднее значение поля personalQualitiesMaximum для всех элементов коллекции");
    }

    @Override
    public boolean execute() {
        int pqmLength = 0;
        Double sumOfPQM = 0.0;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            Double pqm = element.getPersonalQualitiesMaximum();
            sumOfPQM += pqm;
            pqmLength += 1;
        }

        if (pqmLength == 0) {
            getContext().getErrorManager().setException(new AppException("Коллекция пуста. Введите add чтобы добавить эелемент"));
            return false;
        }
        System.out.println("Среднее значение квалификации сотрудников : " + sumOfPQM / pqmLength);
        return true;
    }
}
