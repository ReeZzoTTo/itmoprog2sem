package com.andreysankov.itmoprog2sem.server.managers;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class CollectionManager {
    private LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private Map<String, Discipline> disciplineMap = new HashMap<>();
    private Date initializationDate;

    public CollectionManager() {}

    public void sortCollection() {
        this.collection = this.collection.stream()
            .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setInitializationDate(Context context) {
        FileTime creationFileTime = context.getFileManager().getFileCreationTime();

        this.initializationDate = creationFileTime == null
            ? new Date()
            : new Date(creationFileTime.toMillis());
    }

    public void addElement(LabWork element) {
        this.collection.add(element);

        this.sortCollection();
    }

    public long getCollectionSize() { 
        return collection.size(); 
    }

    public void setDisciplineMap() {
        for (LabWork element : this.collection) {
            Discipline currentDiscipline = element.getDiscipline();
            
            if (currentDiscipline == null) continue;

            disciplineMap.put(currentDiscipline.getName(), currentDiscipline);
        }

        collection.stream()
            .map(LabWork::getDiscipline)
            .filter(discipline -> discipline != null)
            .forEach(discipline -> disciplineMap.put(discipline.getName(), discipline));
    }

    public void addDiscipline(Discipline discipline) {
        this.disciplineMap.put(discipline.getName(), discipline);
    }

    public Map<String, Discipline> getDisciplineMap() { return this.disciplineMap; }

    public void clearCollection() {
        this.collection.clear();
    }

    public Long getLastIdElement() {
        return collection.stream().mapToLong(LabWork::getId).max().orElse(0);
    }

    public Long generateId() {
        return this.getCollectionSize() == 0
            ? 1L
            : this.getLastIdElement() + 1;
    }

    public void setCollection(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        this.sortCollection();
    }

    public LabWork getElementByID(long id) {
        return collection.stream()
            .filter(element -> element.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public boolean deleteElementByID(long id) {
        return this.collection.removeIf(element -> element.getId() == id);
    }

    public Iterator<LabWork> getIterator() { return this.collection.iterator(); } 
    public Date getInitializationDate() { return this.initializationDate; }
    public LinkedHashSet<LabWork> getCollection() { return this.collection; }

}
