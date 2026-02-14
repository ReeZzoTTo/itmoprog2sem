package com.andreysankov.itmoprog2sem.models;

public class Discipline {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long lectureHours; //Поле не может быть null
    private int labsCount;

    public Discipline(
        String name,
        Long lectureHours,
        int labsCount
    ) {
        this.name = name;
        this.lectureHours = lectureHours;
        this.labsCount = labsCount;
    }

    public String getName() { return this.name; }
    public Long getLectureHours() { return this.lectureHours; }
    public int getLabsCount() { return this.labsCount; }
}
