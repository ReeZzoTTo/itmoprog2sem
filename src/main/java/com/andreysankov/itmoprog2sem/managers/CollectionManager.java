package com.andreysankov.itmoprog2sem.managers;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import com.andreysankov.itmoprog2sem.models.Discipline;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class CollectionManager {
    private LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private Map<String, Discipline> disciplineMap = new HashMap<>();
    private Date initializationDate;
    
    public CollectionManager() {}

    public void setInitializationDate(Context context) {
        FileTime creationFileTime = context.getFileManager().getFileCreationTime();

        if (creationFileTime == null) {
            this.initializationDate = new Date();
        } else {
            this.initializationDate = new Date(creationFileTime.toMillis());
        }
    }

    public void addElement(LabWork element) {
        this.collection.add(element);
    }

    public long getCollectionSize() { 
        return collection.size(); 
    }

    public void setDisciplineMap() {
        for (LabWork element : this.collection) {
            Discipline currentDiscipline = element.getDiscipline();
    
            disciplineMap.put(currentDiscipline.getName(), currentDiscipline);
        }
    }

    public void addDiscipline(Discipline discipline) {
        this.disciplineMap.put(discipline.getName(), discipline);
    }

    public Map<String, Discipline> getDisciplineMap() { return this.disciplineMap; }

    public LinkedHashSet<String> getElementsUniqueName() {
        LinkedHashSet<String> elementsUniqueName = new LinkedHashSet<>();

        for (LabWork element : this.collection) {
            elementsUniqueName.add(element.getUniqueName());
        }

        return elementsUniqueName;
    }

    public void clearCollection() {
        this.collection.clear();
    }

    public Long getLastIdElement() {
        return collection.stream().mapToLong(LabWork::getId).max().orElse(0);
    }

    public Long generateId() {
        long collectionSize = this.getCollectionSize();
        if (collectionSize == 0) {
            return 0L;
        } else {
            return this.getLastIdElement() + 1;
        }
    }

    public void setCollection(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
    }

    public LabWork getElementByID(long id) {
        for (LabWork element : collection) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public LabWork getElementByUniqueName(String uniqueName) {
        for (LabWork element : collection) {
            if (element.getUniqueName().equals(uniqueName)) {
                return element;
            }
        }
        return null;
    }

    public boolean deleteElementByID(long id) {
        return this.collection.removeIf(element -> element.getId() == id);
    }

    public Iterator<LabWork> getIterator() { return this.collection.iterator(); } 
    public Date getInitializationDate() { return this.initializationDate; }
    public LinkedHashSet<LabWork> getCollection() { return this.collection; }

}
