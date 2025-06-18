package tools;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EnumUtils {

    public <E extends Enum<E>> E getEnumFromString(Class<E> enumClass, Scanner scanner, List<String> names, String error) {
        String consoleValue = "";

        names = names.stream().map(name -> name.trim().toUpperCase()).collect(Collectors.toList());
        while (true) {
            consoleValue = scanner.next().trim().toUpperCase();

            if (names.contains(consoleValue)) {
                break;
            }
            System.out.println(error);
        }

        return Enum.valueOf(enumClass, consoleValue);
    }
}
