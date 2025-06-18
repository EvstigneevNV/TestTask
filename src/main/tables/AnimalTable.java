package tables;

import data.ColorData;
import db.MySQLConnector;
import animals.Animal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnimalTable extends AbsTable {
    private static final String NAME = "animals";

    public AnimalTable() {
        super(NAME);
    }

    @Override
    public void write(Animal animal) {
        String sql = String.format("INSERT INTO %s (color, name, weight, type, age) " +
                        "VALUES('%s','%s',%d,'%s',%d)",
                NAME,
                -1,
                animal.getName(),
                animal.getWeight(),
                animal.getType(),
                animal.getAge());
        this.dbConnector.execute(sql);
    }

    @Override
    public ArrayList<Animal> read() throws SQLException {
        ArrayList<Animal> animals = new ArrayList<>();
        ResultSet resultSet = this.dbConnector.executeQuery(String.format("SELECT * FROM %s;", NAME));

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            System.out.println("Загружен id: " + id);  // Для отладки
            String type = resultSet.getString("type");
            String name = resultSet.getString("name");
            String color = resultSet.getString("color");
            int age = resultSet.getInt("age");
            int weight = resultSet.getInt("weight");

            Animal animal = new Animal(color, name, weight, type, age);
            animals.add(animal);
        }
        return animals;
    }


    public ArrayList<Animal> read(String animalType) throws SQLException {
        String query = "SELECT * FROM " + NAME + " WHERE type = ?";
        try (PreparedStatement statement = MySQLConnector.getConnection().prepareStatement(query)) {
            statement.setString(1, animalType);
            try (ResultSet resultSet = statement.executeQuery()) {
                return getAnimalsFromResultSet(resultSet);
            }
        }
    }


    public boolean isIdExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM animals WHERE id = ?";
        try (PreparedStatement statement = MySQLConnector.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            System.out.println("Проверка id: " + id + " найдено записей: " + count);  // Для отладки
            return count > 0;
        }
    }

    public boolean isTableEmpty() throws SQLException {
        ArrayList<Animal> animals = read();
        return animals == null || animals.isEmpty();
    }

    @Override
    public void update(Animal animal) throws SQLException {
        if (!isIdExists(animal.getId())) {
            System.out.println("Ошибка: животное с id " + animal.getId() + " не найдено!");
            return;
        }

        String sql = "UPDATE " + NAME + " SET type=?, name=?, color=?, age=?, weight=? WHERE id=?";
        try (PreparedStatement statement = MySQLConnector.getConnection().prepareStatement(sql)) {
            statement.setString(1, animal.getType());
            statement.setString(2, animal.getName());
            statement.setString(3, "");
            statement.setInt(4, animal.getAge());
            statement.setInt(5, animal.getWeight());
            statement.setInt(6, animal.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Обновлено животное с id " + animal.getId());
            } else {
                System.out.println("Ошибка: не удалось обновить id " + animal.getId());
            }
        }
    }

    private ArrayList<Animal> getAnimalsFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Animal> animals = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String type = resultSet.getString("type");
            String name = resultSet.getString("name");
            String colorStr = resultSet.getString("color"); // например "RED"
            int age = resultSet.getInt("age");
            int weight = resultSet.getInt("weight");

            int colorIndex;
            try {
                colorIndex = ColorData.valueOf(colorStr).ordinal();
            } catch (IllegalArgumentException | NullPointerException e) {
                colorIndex = -1; // или другое значение по умолчанию
            }

            Animal animal = new Animal(id, type, name, age, weight, colorIndex);
            animals.add(animal);
        }
        return animals;
    }
}