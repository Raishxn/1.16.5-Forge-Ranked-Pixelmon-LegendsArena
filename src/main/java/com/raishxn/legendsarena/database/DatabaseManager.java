package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static Connection connection;

    public static void connect() {
        String dbType = ConfigManager.get("database.type");
        try {
            if ("mysql".equalsIgnoreCase(dbType)) {
                connectMySQL();
            } else {
                connectSQLite();
            }
            ModFile.LOGGER.info("Conexão com o banco de dados estabelecida com sucesso!");
            initializeTables();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Falha ao conectar com o banco de dados.", e);
        }
    }

    private static void connectSQLite() throws SQLException {
        String dbPath = ConfigManager.get("database.sqlite.path");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    private static void connectMySQL() throws SQLException {
        String host = ConfigManager.get("database.mysql.host");
        int port = ConfigManager.get("database.mysql.port");
        String dbName = ConfigManager.get("database.mysql.database");
        String user = ConfigManager.get("database.mysql.user");
        String password = ConfigManager.get("database.mysql.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?autoReconnect=true";
        connection = DriverManager.getConnection(url, user, password);
    }

    private static void initializeTables() {
        try (Statement statement = connection.createStatement()) {
            // Tabela player_stats (com a nova coluna best_winstreak)
            statement.execute("CREATE TABLE IF NOT EXISTS player_stats (" +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "player_name VARCHAR(16) NOT NULL," +
                    "tier VARCHAR(50) NOT NULL," +
                    "season INT NOT NULL," +
                    "elo INT DEFAULT 1200," +
                    "wins INT DEFAULT 0," +
                    "losses INT DEFAULT 0," +
                    "winstreak INT DEFAULT 0," +
                    "matches_played INT DEFAULT 0," +
                    "best_winstreak INT DEFAULT 0," +
                    "PRIMARY KEY (player_uuid, tier, season));");

            // Tabela player_bans
            statement.execute("CREATE TABLE IF NOT EXISTS player_bans (" +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "tier VARCHAR(50) NOT NULL," +
                    "end_timestamp BIGINT NOT NULL," +
                    "reason TEXT," +
                    "PRIMARY KEY (player_uuid, tier));");

            // Tabela match_history
            statement.execute("CREATE TABLE IF NOT EXISTS match_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid VARCHAR(36)," +
                    "opponent_uuid VARCHAR(36)," +
                    "player_elo_before INT," +
                    "elo_delta INT," +
                    "result VARCHAR(4)," +
                    "timestamp BIGINT);");

            ModFile.LOGGER.info("Tabelas do banco de dados verificadas e prontas.");
        } catch (SQLException e) {
            ModFile.LOGGER.error("Não foi possível criar as tabelas do banco de dados.", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}