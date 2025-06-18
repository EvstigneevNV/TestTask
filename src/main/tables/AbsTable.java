package tables;

import db.IDBConnector;
import db.MySQLConnector;
import animals.Animal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsTable implements ITable{
    protected IDBConnector dbConnector = null;
    private String tableName = "";

    public AbsTable(String tableName) {
        dbConnector = new MySQLConnector();
        this.tableName = tableName;
    }

    @Override
    public void create(List<String> columns) {
        if (!isTableExists()) {
            dbConnector.execute(String.format("CREATE TABLE %s (%s);", tableName, String.join(",", columns)));
        }
    }

    @Override
    public void delete() {
        dbConnector.execute(String.format("drop table if exists %s;",this.tableName));
    }

    public abstract ArrayList<Animal> read() throws SQLException;
    public abstract void update(Animal data) throws SQLException;
    public abstract void write(Animal data);

    public ResultSet selectAll() {
        return dbConnector.executeQuery(String.format("SELECT * FROM  %s;",this.tableName));
    }

    private boolean isTableExists() {
        String query = String.format("SHOW TABLES LIKE '%s';", this.tableName);
        try (ResultSet rs = dbConnector.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}