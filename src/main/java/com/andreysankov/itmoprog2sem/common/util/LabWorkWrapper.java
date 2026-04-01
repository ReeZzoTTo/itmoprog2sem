package com.andreysankov.itmoprog2sem.common.util;

import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "LabWorks")
public class LabWorkWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    private LinkedHashSet<LabWork> labWork;

    public LabWorkWrapper() {
        this.labWork = new LinkedHashSet<>();
    }

    public LabWorkWrapper(LinkedHashSet<LabWork> labWork) {
        this.setLabWork(labWork);
    }

    public LinkedHashSet<LabWork> getLabWork() { return this.labWork; }
    public void setLabWork(LinkedHashSet<LabWork> labWork) { this.labWork = labWork; }
}
