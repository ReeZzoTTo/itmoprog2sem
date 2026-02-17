package com.andreysankov.itmoprog2sem.managers;

import java.util.Date;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.AbstractCommand;
import com.andreysankov.itmoprog2sem.models.Coordinates;
import com.andreysankov.itmoprog2sem.models.Difficulty;
import com.andreysankov.itmoprog2sem.models.Discipline;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class InputManager {
    private Context context;
    private Scanner scanner;

    public void setContext(Context context) {
        this.context = context;
    }

    public void readConsole(Scanner scanner) {
        this.scanner = scanner;

        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            String[] inputSplit = input.split(" ");

            AbstractCommand command = context.getCommandManager().getCommandList().get(inputSplit[0]);

            if (command != null) { 
                context.getCommandManager().setArguments(inputSplit);
                command.execute(); 
                context.getCommandManager().addToHistory(inputSplit[0]);
            }
            else { System.out.println("Неизвестная команда : Введите help"); }            
        }
    }

    public LabWork readLabWork(String uniqueName) {
        InputLabWork inputLabWork = new InputLabWork();
        
        return new LabWork(
            inputLabWork.inputId(),
            inputLabWork.inputName("Укажите название:"),
            inputLabWork.inputCoordinates(),
            inputLabWork.inputCreationDate(),
            inputLabWork.inputMinimalPoint("Укажите минимальное число очков:"),
            inputLabWork.inputPersonalQualitiesMaximum(),
            inputLabWork.inputDifficulty(),
            inputLabWork.inputDiscipline(),
            uniqueName
        );
    }

    private class InputLabWork {
        private Long inputId() {
            long collectionSize = context.getCollectionManager().getCollectionSize();
            if (collectionSize == 0) {
                return 0L;
            } else {
                return context.getCollectionManager().getLastIdElement() + 1;
            }
        }

        private String inputName(String message) {
            System.out.println(message);
            String name = scanner.nextLine();

            while (name == null) {
                System.out.println("Имя не может быть пустым. Повторите ввод:");
                name = scanner.nextLine();
            }

            return name;
        }
        
        private int inputMinimalPoint(String message) {
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

        private Long inputCoordinatesX() {
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

        private int inputCoordinateY() {
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

        private Coordinates inputCoordinates() {
            return new Coordinates(
                this.inputCoordinatesX(),
                this.inputCoordinateY()
            );
        }
        
        private Date inputCreationDate() {
            return new Date();
        }
        
        private Double inputPersonalQualitiesMaximum() {
            System.out.println("Введите максимальную квалификацию персонала (число):");
            Double personalQualitiesMaximum;

            while (true) {
                try {
                    personalQualitiesMaximum = Double.parseDouble(scanner.nextLine());
                    if (personalQualitiesMaximum <= 0) { System.out.println("Число должно быть >0"); }
                    else { break; }
                } catch (NumberFormatException e) {
                    System.out.println("Некорректная форма числа. Повторите ввод:");
                }
            }

            return personalQualitiesMaximum;
        }
        
        private Difficulty inputDifficulty() {
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
    
        private Discipline inputDiscipline() {
            return new Discipline(
                this.inputDisciplineName(),
                this.inputDisciplineLectureHours(),
                this.inputDisciplineLabsCount()
            );
        }

        private String inputDisciplineName() {
            return inputName("Создание Дисциплины -> Укажите название дисциплины");
        }

        private Long inputDisciplineLectureHours() {
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
      
        private int inputDisciplineLabsCount() {
            return this.inputMinimalPoint("Создание Дисциплины -> Укажите кол-во лабораторных работ");
        }
    }
}
