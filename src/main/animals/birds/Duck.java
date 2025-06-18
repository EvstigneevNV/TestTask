package animals.birds;

import animals.Animal;
import data.ColorData;

public class Duck extends Animal implements Flying {
    public Duck(String name, int age, int weight, ColorData colorData) {
        super(name, age, weight, colorData);

    }

    public Duck(String color, String name, int weight, String type, int age) {
        super();
    }

    @Override
    public void fly() {
        System.out.println("Я лечу");
    }


    @Override
    public void say() {
        System.out.println("Кря");
    }


}