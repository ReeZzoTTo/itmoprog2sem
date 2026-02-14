package com.andreysankov.itmoprog2sem.managers;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.models.LabWork;

public class CollectionManager {
    private LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private Date initializationDate;

    public CollectionManager() {
        if (this.getCollectionSize() == 0) initializationDate = new Date();
    }

    public void addElement(LabWork element) {
        this.collection.add(element);
    }

    public long getCollectionSize() { 
        return collection.size(); 
    }

    public void clearCollection() {
        this.collection.clear();
    }

    public Long getLastIdElement() {
        return this.collection.getLast().getId();
    }

    public LabWork getElementByID(long id) {
        for (LabWork element : collection) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public boolean deleteElementByID(long id) {
        return this.collection.removeIf(element -> element.getId() == id);
    }

    public Date getInitializationDate() { return this.initializationDate; }
    public LinkedHashSet<LabWork> getCollection() { return this.collection; }

}
