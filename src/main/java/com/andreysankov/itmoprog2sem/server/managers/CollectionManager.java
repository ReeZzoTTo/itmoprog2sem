package com.andreysankov.itmoprog2sem.server.managers;


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

    public synchronized void sortCollection() {
        this.collection = this.collection.stream()
            .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public synchronized void setInitializationDate() {
        this.initializationDate = new Date();
    }

    public synchronized void addElement(LabWork element) {
        this.collection.add(element);

        if (element.getDiscipline() != null) {
            this.addDiscipline(element.getDiscipline());
        }

        this.sortCollection();
    }

    public synchronized long getCollectionSize() { 
        return collection.size(); 
    }

    public synchronized void setDisciplineMap() {
        this.disciplineMap.clear();

        collection.stream()
            .map(LabWork::getDiscipline)
            .filter(discipline -> discipline != null)
            .forEach(discipline -> disciplineMap.put(discipline.getName(), discipline));
    }

    public synchronized void addDiscipline(Discipline discipline) {
        if (discipline != null) {
            this.disciplineMap.put(discipline.getName(), discipline);
        }
    }

    public synchronized Map<String, Discipline> getDisciplineMap() { return new HashMap<>(this.disciplineMap); }

    public synchronized void clearCollection() {
        this.collection.clear();
        this.disciplineMap.clear();
    }

    // public Long getLastIdElement() {
    //     return collection.stream().mapToLong(LabWork::getId).max().orElse(0);
    // }

    // public Long generateId() {
    //     return this.getCollectionSize() == 0
    //         ? 1L
    //         : this.getLastIdElement() + 1;
    // }

    public synchronized void setCollection(LinkedHashSet<LabWork> collection) {
        this.collection = collection == null ? new LinkedHashSet<>() : collection;
        this.sortCollection();
    }

    public synchronized LabWork getElementByID(long id) {
        return collection.stream()
            .filter(element -> element.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public synchronized boolean deleteElementByID(long id) {
        boolean removed = this.collection.removeIf(element -> element.getId() == id);

        if (removed) this.setDisciplineMap();

        return removed;
    }

    public synchronized int deleteElementsByOwner(String ownerLogin) {
        int oldSize = this.collection.size();

        this.collection.removeIf(element ->
            ownerLogin.equals(element.getOwnerLogin())
        );

        this.setDisciplineMap();

        return oldSize - this.collection.size();
    }

    public synchronized int deleteLowerByOwner(int minimalPoint, String ownerLogin) {
        int oldSize = this.collection.size();

        this.collection.removeIf(element ->
            ownerLogin.equals(element.getOwnerLogin())
                && element.getMinimalPoint() < minimalPoint
        );

        this.setDisciplineMap();

        return oldSize - this.collection.size();
    }

    public synchronized Iterator<LabWork> getIterator() { return new LinkedHashSet<>(this.collection).iterator(); } 
    public synchronized Date getInitializationDate() { return this.initializationDate; }
    public synchronized LinkedHashSet<LabWork> getCollection() { return new LinkedHashSet<>(this.collection); }
}
