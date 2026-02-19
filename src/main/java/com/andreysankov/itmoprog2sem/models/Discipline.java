package com.andreysankov.itmoprog2sem.models;

public class Discipline {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long lectureHours; //Поле не может быть null
    private int labsCount;

    public Discipline() {}

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Discipline that = (Discipline) obj;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
