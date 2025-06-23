package ru.job4j.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {

    private Connection connection;

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection connection) {
        this.connection = connection;
    }

    private void init() {
        try {
            connection = loadConnection();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String loadSysEnvIfNullThenConfig(String sysEnv, String key, Properties config) {
        String value = System.getenv(sysEnv);
        if (value == null) {
            value = config.getProperty(key);
        }
        return value;
    }

    private Connection loadConnection() throws ClassNotFoundException, SQLException {
        var config = new Properties();
        try (InputStream in = StartUI.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            config.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String url = loadSysEnvIfNullThenConfig("JDBC_URL", "url", config);
        String username = loadSysEnvIfNullThenConfig("JDBC_USERNAME", "username", config);
        String password = loadSysEnvIfNullThenConfig("JDBC_PASSWORD", "password", config);
        String driver = loadSysEnvIfNullThenConfig("JDBC_DRIVER", "driver-class-name", config);
        System.out.println("url=" + url);
        Class.forName(driver);
        return DriverManager.getConnection(
                url, username, password
        );
    }

    private List<Item> getListOfItems(String query) {
        List<Item> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.add(
                        new Item(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getTimestamp("created").toLocalDateTime(),
                                new ArrayList<>() /* Todo: убрать костыль */
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO items(name, created) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                item.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean result;
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE items SET name = ?, created = ? WHERE id = ?"
        )) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            statement.setInt(3, id);
            result = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> findAll() {
        return getListOfItems("SELECT * FROM items");
    }

    @Override
    public List<Item> findByName(String key) {
        return getListOfItems(String.format("SELECT * FROM items WHERE name = '%s'", key));
    }

    @Override
    public Item findById(int id) {
        Item result = null;
        List<Item> list = getListOfItems(String.format("SELECT * FROM items WHERE id = %d", id));
        if (!list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }
}