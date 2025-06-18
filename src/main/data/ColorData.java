package data;

public enum ColorData {

    WHITE("Белый"),
    RED("Красный");

    private String name;

    ColorData(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

}

