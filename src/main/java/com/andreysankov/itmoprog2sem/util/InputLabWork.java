package com.andreysankov.itmoprog2sem.util;

import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.Coordinates;
import com.andreysankov.itmoprog2sem.models.Difficulty;
import com.andreysankov.itmoprog2sem.models.Discipline;

public class InputLabWork {
    private Context context;
    private Scanner scanner;

    public InputLabWork(Context context) {
        this.context = context;
        this.scanner = this.context.getScanner();
    }    
    
    public String inputName(String message) {
        System.out.println(message);
        String name = scanner.nextLine();

        while (name == null || name.length() == 0 || name.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым. Повторите ввод:");
            name = scanner.nextLine();
        }

        return name;
    }
    
    public int inputMinimalPoint(String message) {
        System.out.println(message);
        int minimalPoint;

        while (true) {
            try {
                minimalPoint = Integer.parseInt(scanner.nextLine());
                if (minimalPoint <= 0) { System.out.println("Число должно быть >0. Повторите ввод:"); }
                else { break; }
            } catch (NumberFormatException e) {
                System.out.println("Некорректная форма числа. Повторите ввод:");
            }    
        }

        return minimalPoint;
    }

    public Long inputCoordinatesX() {
        System.out.println("Задание местоположения -> Координата x:");
        Long x;

        while (true) {
            try {
                x = Long.parseLong(scanner.nextLine());
                break;          //!
            } catch (NumberFormatException e) {
                System.out.println("Неккоректная форма числа. Повторите ввод:");
            }
        }
        return x;
    }

    public int inputCoordinateY() {
        System.out.println("Задание местоположения -> Координата y:");
        int y;

        while (true) {
            try {
                y = Integer.parseInt(scanner.nextLine());
                break;          //!
            } catch (NumberFormatException e) {
                System.out.println("Неккоректная форма числа. Повторите ввод:");
            }
        }
        return y;
    }

    public Coordinates inputCoordinates() {
        return new Coordinates(
            this.inputCoordinatesX(),
            this.inputCoordinateY()
        );
    }
    
    public Date inputCreationDate() {
        return new Date();
    }
    
    public Double inputPersonalQualitiesMaximum() {
        System.out.println("Введите максимальную квалификацию персонала (число):");
        Double personalQualitiesMaximum;

        while (true) {
            try {
                String input = scanner.nextLine();

                if (input.trim().isEmpty()) return null;

                personalQualitiesMaximum = Double.parseDouble(input);
                
                if (personalQualitiesMaximum <= 0) { System.out.println("Число должно быть >0"); }
                else { break; }
            } catch (NumberFormatException e) {
                System.out.println("Некорректная форма числа. Повторите ввод:");
            }
        }

        return personalQualitiesMaximum;
    }
    
    public Difficulty inputDifficulty() {
        System.out.println("Укажите сложность (VERY_EASY, HARD, IMPOSSIBLE, INSANE, TERRIBLE) : ");
        Difficulty difficulty;

        while (true) {
            try {
                String input = scanner.nextLine();
                difficulty = Difficulty.valueOf(input.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректное значение. Повторите ввод:");
            }
        }

        return difficulty;
    }

    public Discipline inputDiscipline() {
        System.out.println("Хотите указать дисциплину? (yes, если да)");

        String yesOrNot = scanner.nextLine();

        if (!yesOrNot.equalsIgnoreCase("yes")) return null;

        String disciplineName = this.inputDisciplineName();
        Map<String, Discipline> disciplineMap = context.getCollectionManager().getDisciplineMap();

        if (disciplineMap.containsKey(disciplineName)) {
            System.out.println("Текущая дисциплина уже существует");
            
            return disciplineMap.get(disciplineName);    
        }

        Discipline returnableDiscipline = new Discipline(
            disciplineName,
            this.inputDisciplineLectureHours(),
            this.inputDisciplineLabsCount()
        );

        context.getCollectionManager().addDiscipline(returnableDiscipline);

        return returnableDiscipline;
    }

    public String inputDisciplineName() {
        System.out.println("Создание Дисциплины -> Укажите название дисциплины");
        String disciplineName = scanner.nextLine();

        while (disciplineName == null || disciplineName.length() == 0 || disciplineName.trim().isEmpty()) {
            System.out.println("Имя дисциплины не может быть пустым");
            disciplineName = scanner.nextLine();
        }

        return disciplineName;
    }

    public Long inputDisciplineLectureHours() {
        System.out.println("Создание Дисциплины -> Укажите кол-во академических часов");
        Long lectureHours;

        while (true) {
            try {  
                lectureHours = Long.parseLong(scanner.nextLine());
                if (lectureHours <= 0) { System.out.println("Число должно быть >0. Повторите ввод:"); }
                else { break; }
            } catch (NumberFormatException e) {
                System.out.println("Некорректная форма числа. Повторите ввод:");
            }
        }

        return lectureHours;
    }
    
    public int inputDisciplineLabsCount() {
        return this.inputMinimalPoint("Создание Дисциплины -> Укажите кол-во лабораторных работ");
    }
}
