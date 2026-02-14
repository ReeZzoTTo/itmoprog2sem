package com.andreysankov.itmoprog2sem.managers;

import java.util.Date;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.models.LabWork;

public class CollectionManager {
    private LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private Date initializationDate;

    public void addElement(LabWork element) {
        this.collection.add(element);
    }

    public long getCollectionSize() { 
        if (collection == null) {
            return 0;
        }
        return collection.size(); 
    }

}
