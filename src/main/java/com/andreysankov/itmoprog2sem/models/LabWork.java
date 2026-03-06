package com.andreysankov.itmoprog2sem.models;

import java.util.Date;

public class LabWork implements Comparable<LabWork>{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int minimalPoint; //Значение поля должно быть больше 0
    private Double personalQualitiesMaximum; //Поле может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Discipline discipline; //Поле может быть null
    private String uniqueName;

    public LabWork() {}

    public LabWork(
        long id, String name, Coordinates coordinates, Date creationDate, int minimalPoint,
        Double personalQualitiesMaximum, Difficulty difficulty, Discipline discipline, String uniqueName
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.personalQualitiesMaximum = personalQualitiesMaximum;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.uniqueName = uniqueName;
    }

    @Override
    public int compareTo(LabWork other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "\nIdentifier Name        : " + this.getUniqueName()
            + "\nID                     : " + this.getId()
            + "\nName                   : " + this.getName()
            + "\nCoordinates            : X = " + this.getCoordinates().getX()
            + "\n                         Y = " + this.getCoordinates().getY()
            + "\nCreation Date          : " + this.getDate()
            + "\nMinimal Points         : " + this.getMinimalPoint()
            + "\nMax Personal Qualities : " + this.getPersonalQualitiesMaximum()
            + "\nDifficulty             : " + this.getDifficulty()
            + (this.getDiscipline() == null ? "" : "\nDiscipline             : Name = " + this.getDiscipline().getName()
            + "\n                         Lecture Hours = " + this.getDiscipline().getLectureHours()
            + "\n                         Labs Count = " + this.getDiscipline().getLabsCount());
    }

    public void setId(long id) { this.id = id; }
    public void setDate(Date date) { this.creationDate = date; }

    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public Coordinates getCoordinates() { return this.coordinates; }
    public Date getDate() { return this.creationDate; }
    public int getMinimalPoint() { return this.minimalPoint; }
    public Double getPersonalQualitiesMaximum() { return this.personalQualitiesMaximum; }
    public Difficulty getDifficulty() { return this.difficulty; }
    public Discipline getDiscipline() { return this.discipline; }
    public String getUniqueName() { return this.uniqueName; }
}
