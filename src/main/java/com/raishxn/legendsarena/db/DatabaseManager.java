package com.raishxn.legendsarena.db;

import com.raishxn.legendsarena.legendsarena;
import com.raishxn.legendsarena.config.CoreConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private final CoreConfig.DatabaseConfig dbConfig;
    private HikariDataSource dataSource;

    public DatabaseManager(CoreConfig config) {
        if (config == null || config.getDatabase() == null) {
            throw new IllegalStateException("Configuração principal (CoreConfig) ou configuração de DB não foi carregada. Verifique o config.yml.");
        }
        this.dbConfig = config.getDatabase();
    }

    public void connect() {
        if (!dbConfig.getType().equalsIgnoreCase("mysql")) {
            legendsarena.LOGGER.error("Tipo de banco de dados não suportado: " + dbConfig.getType() + ". Use 'mysql'.");
            return;
        }

        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getName());
            config.setUsername(dbConfig.getUser());
            config.setPassword(dbConfig.getPass());
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setMaximumPoolSize(10); // Recomenda o PDF

            this.dataSource = new HikariDataSource(config);
            legendsarena.LOGGER.info("Conexão com o banco de dados MySQL estabelecida com sucesso!");

        } catch (Exception e) {
            legendsarena.LOGGER.error("Falha ao conectar ao banco de dados MySQL.", e);
            this.dataSource = null;
        }
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            legendsarena.LOGGER.info("Conexão com o banco de dados fechada.");
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Pool de conexão (DataSource) não foi inicializado.");
        }
        return dataSource.getConnection();
    }

    // Método para criar as tabelas (baseado no PDF)
    public void createTables() {
        if (dataSource == null) {
            legendsarena.LOGGER.error("Não é possível criar tabelas, o banco de dados não está conectado.");
            return;
        }

        // Tabela de Jogadores
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid VARCHAR(36) PRIMARY KEY NOT NULL," +
                "username VARCHAR(16) NOT NULL," +
                "current_elo INT DEFAULT 1000," +
                "current_tier VARCHAR(50) DEFAULT 'Ferro'," +
                "last_seen TIMESTAMP" +
                ");";

        // Tabela de Estatísticas por Temporada
        String createPlayerStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "stat_id INT AUTO_INCREMENT PRIMARY KEY," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "season_id INT NOT NULL," +
                "wins INT DEFAULT 0," +
                "losses INT DEFAULT 0," +
                "win_streak INT DEFAULT 0," +
                "FOREIGN KEY (player_uuid) REFERENCES players(uuid)" +
                ");";

        // Tabela de Temporadas
        String createSeasonsTable = "CREATE TABLE IF NOT EXISTS seasons (" +
                "season_id INT AUTO_INCREMENT PRIMARY KEY," +
                "season_name VARCHAR(100) NOT NULL," +
                "start_date TIMESTAMP," +
                "end_date TIMESTAMP," +
                "is_active BOOLEAN DEFAULT false" +
                ");";

        // Tabela de Bans de Jogador
        String createBansTable = "CREATE TABLE IF NOT EXISTS bans (" +
                "ban_id INT AUTO_INCREMENT PRIMARY KEY," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "reason TEXT," +
                "banned_by_uuid VARCHAR(36)," +
                "expiry_date TIMESTAMP," +
                "FOREIGN KEY (player_uuid) REFERENCES players(uuid)" +
                ");";

        // Tabela de Histórico de Partidas
        String createMatchHistoryTable = "CREATE TABLE IF NOT EXISTS match_history (" +
                "match_id INT AUTO_INCREMENT PRIMARY KEY," +
                "player1_uuid VARCHAR(36) NOT NULL," +
                "player2_uuid VARCHAR(36) NOT NULL," +
                "winner_uuid VARCHAR(36)," +
                "elo_change_p1 INT," +
                "elo_change_p2 INT," +
                "match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "season_id INT" +
                ");";

        // Executa as queries
        try (Connection conn = getConnection()) {
            conn.prepareStatement(createPlayersTable).execute();
            conn.prepareStatement(createPlayerStatsTable).execute();
            conn.prepareStatement(createSeasonsTable).execute();
            conn.prepareStatement(createBansTable).execute();
            conn.prepareStatement(createMatchHistoryTable).execute();

            legendsarena.LOGGER.info("Tabelas do banco de dados verificadas/criadas com sucesso.");

        } catch (SQLException e) {
            legendsarena.LOGGER.error("Falha ao criar as tabelas do banco de dados.", e);
        }
    }
}