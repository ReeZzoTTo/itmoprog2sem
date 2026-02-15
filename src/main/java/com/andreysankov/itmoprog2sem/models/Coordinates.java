package com.andreysankov.itmoprog2sem.models;

public class Coordinates {
    private Long x; //Поле не может быть null
    private int y;

    public Coordinates() {}
 
    public Coordinates(Long x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(Long x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public Long getX() { return this.x; }
    public int getY() { return this.y; }
}
