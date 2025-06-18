import animals.Animal;
import data.AnimalTypeData;
import data.ColorData;
import data.CommandData;
import factory.AnimalFactory;
import tables.AnimalTable;
import tools.EnumUtils;
import tools.NumberValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner inputScanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных
    private static NumberValidator validator = new NumberValidator(); // Создание объекта для валидации чисел

    public static void main(String... args) throws SQLException {
        AnimalTable animalTable = new AnimalTable();
        System.out.println(animalTable.read());

        List<Animal> animalList = new ArrayList<>(); // Список для хранения животных
        List<String> commandList = new ArrayList<>(); // Список доступных команд
        EnumUtils enumHelper = new EnumUtils(); // Утилита для работы с перечислениями

        // Заполнение списка команд из перечисления CommandData
        for (CommandData command : CommandData.values()) {
            commandList.add(command.name().toLowerCase());
        }

        while (true) { // Бесконечный цикл для обработки команд
            System.out.println(String.format("Введите команду: %s", String.join("/", commandList)));

            // Получение команды от пользователя с помощью EnumUtils
            CommandData selectedCommand = (CommandData) enumHelper.getEnumFromString(
                    CommandData.class,
                    inputScanner,
                    commandList,
                    "Некорректная команда. Пожалуйста, попробуйте снова."
            );

            switch (selectedCommand) { // Обработка выбранной команды
                case ADD: { // Команда добавления животного
                    List<String> animalTypes = new ArrayList<>(); // Список типов животных
                    for (AnimalTypeData type : AnimalTypeData.values()) {
                        animalTypes.add(type.name().toLowerCase()); // Добавление типов в список
                    }

                    System.out.printf("Введите тип животного: %s\n", String.join(",", animalTypes));
                    // Получение типа животного от пользователя
                    AnimalTypeData selectedAnimalType = (AnimalTypeData) enumHelper.getEnumFromString(
                            AnimalTypeData.class,
                            inputScanner,
                            animalTypes,
                            "Некорректный тип животного."
                    );

                    System.out.println("Введите имя животного:");
                    String animalName = inputScanner.next(); // Чтение имени

                    // Запрос возраста животного с проверкой корректности ввода
                    int age = requestAnimalAgeOrWeight("Введите возраст животного:", "Некорректный возраст. Пожалуйста, попробуйте снова.");
                    // Запрос веса животного с проверкой корректности ввода
                    int weight = requestAnimalAgeOrWeight("Введите вес животного:", "Некорректный вес. Пожалуйста, попробуйте снова.");

                    List<String> colorOptions = new ArrayList<>(); // Список цветов животных
                    for (ColorData color : ColorData.values()) {
                        colorOptions.add(color.name().toLowerCase()); // Добавление цветов в список
                    }

                    System.out.printf("Введите цвет животного: %s\n", String.join(",", colorOptions));
                    // Получение цвета животного от пользователя
                    ColorData selectedColor = enumHelper.getEnumFromString(
                            ColorData.class,
                            inputScanner,
                            colorOptions,
                            "Некорректный цвет животного."
                    );

                    // Создание нового животного с помощью фабрики и добавление его в список
                    Animal newAnimal = AnimalFactory.createAnimal(selectedAnimalType,animalName, age, weight, selectedColor);
                    animalList.add(newAnimal);
                    break;
                }

                case LIST: {
                    for (Animal animal : animalList) {
                        System.out.println(animal.toString()); // Вывод информации о каждом животном
                    }
                    break;
                }

                case EXIT: {
                    System.exit(0); // Завершение работы программы
                }
            }
        }
    }

    private static int requestAnimalAgeOrWeight(String promptMessage, String errorMessage) {
        while (true) { // Бесконечный цикл для запроса ввода
            System.out.println(promptMessage); // Вывод сообщения с запросом ввода
            String userInput = inputScanner.next(); // Чтение ввода пользователя
            if (!validator.isNumber(userInput)) { // Проверка корректности ввода
                System.out.println(errorMessage); // Вывод сообщения об ошибке, если ввод некорректен
                continue; // Повторить запрос ввода
            }
            return Integer.parseInt(userInput); // Преобразование ввода в целое число и возврат значения
        }
    }
}
