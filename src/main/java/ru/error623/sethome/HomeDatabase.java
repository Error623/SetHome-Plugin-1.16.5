package ru.error623.sethome;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HomeDatabase {
    private final JavaPlugin plugin;
    private Connection connection;

    public HomeDatabase(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    private File getDb() {
        File folderDb = plugin.getDataFolder();

        if (!folderDb.exists()) {
            folderDb.mkdirs();
        }

        File database = new File(folderDb, "homes.db");
        return database;
    }


    private void connect() {
        try {
            String url = "jdbc:sqlite:" + getDb().getAbsolutePath();
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void open() {
        connect();

        if (connection != null ) {
            createTable();
        }

    }


    public boolean saveHome(String player_name, String uuid, String home_name, String world_name, double x, double y, double z) {
        if(connection == null) {
            plugin.getLogger().severe("Failed to connect a database");
            return false;
        }

        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO homes (player_name, uuid, home_name, world_name, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, player_name);
            statement.setString(2, uuid);
            statement.setString(3, home_name);
            statement.setString(4, world_name);
            statement.setDouble(5, x);
            statement.setDouble(6, y);
            statement.setDouble(7, z);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delHome(String uuid, String home_name) {
        if(connection == null) {
            plugin.getLogger().severe("Failed to connect a database");
            return false;
        }

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM homes WHERE uuid = ? AND home_name = ?")) {
            statement.setString(1, uuid);
            statement.setString(2, home_name);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HomeData> getHomes(String uuid) {
        List<HomeData> homes = new ArrayList<>();

        if(connection == null) {
            plugin.getLogger().severe("Failed to connect a database");
            return homes;
        }

        String sql = "SELECT home_name, world_name, x, y, z FROM homes WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                 String homeName =  resultSet.getString("home_name");
                 String worldName =  resultSet.getString("world_name");
                 double x =  resultSet.getDouble("x");
                 double y =  resultSet.getDouble("y");
                 double z =  resultSet.getDouble("z");
                    HomeData home = new HomeData(homeName, worldName, x, y, z);
                    homes.add(home);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    public HomeData getHome(String uuid, String homeName) {
        if(connection == null) {
            plugin.getLogger().severe("Failed to connect a database");
            return null;
        }
        String sql = "SELECT home_name, world_name, x, y, z FROM homes WHERE uuid = ? AND home_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid);
            statement.setString(2, homeName);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    String name = resultSet.getString("home_name");
                    String world = resultSet.getString("world_name");
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");
                    return new HomeData(name, world, x, y, z);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void closeDb() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                                CREATE TABLE IF NOT EXISTS homes(
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    player_name TEXT NOT NULL,
                                    uuid TEXT NOT NULL,
                                    home_name TEXT NOT NULL,
                                    world_name TEXT NOT NULL,
                                    x REAL NOT NULL,
                                    y REAL NOT NULL,
                                    z REAL NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    UNIQUE(uuid, home_name)
                                )""");
            plugin.getLogger().info("A database is successfully created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
