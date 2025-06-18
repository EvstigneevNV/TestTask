package factory;

import animals.Animal;
import animals.birds.Duck;
import animals.pets.Cat;
import animals.pets.Dog;
import data.AnimalTypeData;
import data.ColorData;

import java.util.Arrays;
import java.util.List;

public class AnimalFactory {


    public static Animal createAnimal(AnimalTypeData selectedAnimalType, String name, int age, int weight, ColorData color) {
        switch (selectedAnimalType.toString()) {
            case "CAT":
                return new Cat(color.toString(), name, weight, selectedAnimalType.toString(), age);
            case "DOG":
                return new Dog(color.toString(), name, weight, selectedAnimalType.toString(), age);
            case "DUCK":
                return new Duck(color.toString(), name, weight, selectedAnimalType.toString(), age);
            default:
                throw new IllegalArgumentException("Неизвестный тип животного: " + selectedAnimalType.toString());
        }
    }

    public static final List<String> ANIMAL_TYPES = Arrays.asList("CAT", "DOG", "DUCK");

    public Animal create(AnimalTypeData selectedAnimalType) {
        return null;
    }

}
