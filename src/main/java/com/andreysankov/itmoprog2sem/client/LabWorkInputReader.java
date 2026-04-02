package com.andreysankov.itmoprog2sem.client;

import com.andreysankov.itmoprog2sem.client.utils.InputLabWork;
import com.andreysankov.itmoprog2sem.common.util.LineInput;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class LabWorkInputReader {
    private final LineInput console;

    public LabWorkInputReader(LineInput console) {
        this.console = console;
    }

    public LabWork getLabWork() {
        InputLabWork inputLabWork = new InputLabWork(this.console);
        
        inputLabWork.disableHistory();

        LabWork labwork = new LabWork(
            0,
            inputLabWork.inputName("Укажите название:"),
            inputLabWork.inputCoordinates(),
            null,
            inputLabWork.inputMinimalPoint("Укажите минимальное число очков:"),
            inputLabWork.inputPersonalQualitiesMaximum(),
            inputLabWork.inputDifficulty(),
            inputLabWork.inputDiscipline()
        );

        inputLabWork.enableHistory();
        
        return labwork;
    }
}
