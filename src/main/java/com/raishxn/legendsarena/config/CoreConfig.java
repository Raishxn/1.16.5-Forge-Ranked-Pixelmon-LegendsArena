package com.raishxn.legendsarena.config;

import java.util.List;
import java.util.Map;

public class CoreConfig {

    private ServerConfig server;
    private DatabaseConfig database;
    private LoggingConfig logging;
    private IntegrationsConfig integrations;
    private ExtrasConfig extras;
    private DbSchemaConfig dbSchema;
    private Map<String, String> files;

    // Getters e Setters para todos os campos acima...
    public ServerConfig getServer() { return server; }
    public void setServer(ServerConfig server) { this.server = server; }
    public DatabaseConfig getDatabase() { return database; }
    public void setDatabase(DatabaseConfig database) { this.database = database; }
    public LoggingConfig getLogging() { return logging; }
    public void setLogging(LoggingConfig logging) { this.logging = logging; }
    public IntegrationsConfig getIntegrations() { return integrations; }
    public void setIntegrations(IntegrationsConfig integrations) { this.integrations = integrations; }
    public ExtrasConfig getExtras() { return extras; }
    public void setExtras(ExtrasConfig extras) { this.extras = extras; }
    public DbSchemaConfig getDbSchema() { return dbSchema; }
    public void setDbSchema(DbSchemaConfig dbSchema) { this.dbSchema = dbSchema; }
    public Map<String, String> getFiles() { return files; }
    public void setFiles(Map<String, String> files) { this.files = files; }

    // --- Classes Internas para cada seção ---
    public static class ServerConfig {
        private String name;
        private String version;
        private String locale;
        // Getters/Setters...
    }

    public static class DatabaseConfig {
        private String type;
        private String host;
        private int port;
        private String name;
        private String user;
        private String pass;
        private PoolConfig pool;
        // Getters/Setters...
    }

    public static class PoolConfig {
        private int maxConnections;
        private int minIdle;
        private int connectionTimeout;
        // Getters/Setters...
    }

    public static class LoggingConfig {
        private String level;
        private boolean logMatches;
        private String logDir;
        // Getters/Setters...
    }

    public static class IntegrationsConfig {
        private DiscordConfig discord;
        private PaymentsConfig payments;
        // Getters/Setters...
    }

    public static class DiscordConfig {
        private boolean enabled;
        private String webhookUrl;
        private boolean postMatchSummary;
        // Getters/Setters...
    }

    public static class PaymentsConfig {
        private boolean enabled;
        private String provider;
        // Getters/Setters...
    }

    public static class ExtrasConfig {
        private ApiConfig api;
        private SpectatorModeConfig spectatorMode;
        private AutoReportCheatConfig autoReportCheat;
        // Getters/Setters...
    }

    public static class ApiConfig {
        private boolean enabled;
        private int port;
        private String token;
        // Getters/Setters...
    }

    public static class SpectatorModeConfig {
        private boolean enable;
        private List<String> allowedGroups;
        // Getters/Setters...
    }

    public static class AutoReportCheatConfig {
        private boolean enabled;
        private double thresholdSuspicious;
        // Getters/Setters...
    }

    public static class DbSchemaConfig {
        private String players, stats, matches, seasons, bans, queues, penalties;
        // Getters/Setters...
    }
}