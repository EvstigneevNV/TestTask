package animals;

import data.ColorData;

public class Animal {

    private int id = -1;
    private String type = "";
    private String name = "";
    private int age = -1;
    private int weight = -1;
    private String color = "";

    public Animal(int id, String type, String name, int age, int weight, int colorData) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.age = age;
        this.weight = weight;
//        this.color = colorData;
    }

    public Animal(String type, String name, int age, int weight, int colorData) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.weight = weight;
//        this.color = colorData;
    }

    public Animal(String color, String name, int weight, String type, int age) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public Animal() {

    }

    public Animal(String name, int age, int weight, ColorData colorData) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

//    public String getColor() {
//        return ColorData.values()[color].toString();
//    }
//
//    public void setColor(ColorData color) {
//        this.color = color.ordinal();
//    }

    public void say() {
        System.out.println("Я говорю");
    }

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    @Override
    public String toString() {
        return String.format("Привет! Я %s. Меня зовут %s, мне %d %s, я вешу - %d кг, мой цвет - %s",
                type, name, age, getYearPadej(), weight, color);
    }

    private String getYearPadej() {
        if (age >= 11 && age <= 14) {
            return "лет";
        }

        int ostatok = age % 10;
        if (ostatok == 0) {
            return "лет";
        }

        if (ostatok == 1) {
            return "год";
        }

        if (ostatok >= 2 && ostatok <= 4) {
            return "года";
        }

        return "лет";
    }
}
