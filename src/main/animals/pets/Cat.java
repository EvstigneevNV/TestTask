package animals.pets;

import animals.Animal;

public class Cat extends Animal {
    public Cat(String color, String name, int weight, String type, int age) {
        super(color, name, weight, type, age);
    }

    @Override
    public void say(){
        System.out.println("Мяу");
    }
}