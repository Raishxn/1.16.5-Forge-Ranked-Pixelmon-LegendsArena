package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private final ConfigManager configManager;
    private HikariDataSource dataSource;

    public DatabaseManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void initializeDatabase() {
        String dbType = this.configManager.getConfig().getDatabase().getType();

        HikariConfig config = new HikariConfig();

        if (dbType.equalsIgnoreCase("sqlite")) {
            File dbFile = new File("config/" + ModFile.MOD_ID, "legendsarena.db");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } else if (dbType.equalsIgnoreCase("mysql")) {
            config.setJdbcUrl("jdbc:mysql://" +
                    this.configManager.getConfig().getDatabase().getHost() + ":" +
                    this.configManager.getConfig().getDatabase().getPort() + "/" +
                    this.configManager.getConfig().getDatabase().getDatabase());
            config.setUsername(this.configManager.getConfig().getDatabase().getUsername());
            config.setPassword(this.configManager.getConfig().getDatabase().getPassword());
        } else {
            ModFile.LOGGER.error("Tipo de base de dados inválido: " + dbType);
            return;
        }

        this.dataSource = new HikariDataSource(config);
        createTables();
    }

    private void createTables() {
        // Query atualizada para incluir a queue_id
        String createPlayerStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "player_name VARCHAR(16) NOT NULL," +
                "queue_id VARCHAR(50) NOT NULL," + // <-- NOVA COLUNA
                "elo INT NOT NULL," +
                "wins INT NOT NULL," +
                "losses INT NOT NULL," +
                "winstreak INT NOT NULL," +
                "matches_played INT NOT NULL," +
                "season INT NOT NULL" +
                ");";

        String createPlayerBansTable = "CREATE TABLE IF NOT EXISTS player_bans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "reason TEXT NOT NULL," +
                "banned_by_uuid VARCHAR(36) NOT NULL," +
                "banned_at BIGINT NOT NULL," +
                "expires_at BIGINT NOT NULL" +
                ");";

        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(createPlayerStatsTable)) {
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement(createPlayerBansTable)) {
                ps.execute();
            }
            ModFile.LOGGER.info("Tabelas da base de dados verificadas/criadas com sucesso.");
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao criar as tabelas da base de dados.", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void close() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }
}