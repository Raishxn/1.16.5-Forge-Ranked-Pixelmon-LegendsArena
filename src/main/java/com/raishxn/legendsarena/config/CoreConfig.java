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

    // --- Classes Internas para cada seção (COM GETTERS/SETTERS) ---

    public static class ServerConfig {
        private String name;
        private String version;
        private String locale;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getLocale() { return locale; }
        public void setLocale(String locale) { this.locale = locale; }
    }

    public static class DatabaseConfig {
        private String type;
        private String host;
        private int port;
        private String name;
        private String user;
        private String pass;
        private PoolConfig pool;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }
        public String getPass() { return pass; }
        public void setPass(String pass) { this.pass = pass; }
        public PoolConfig getPool() { return pool; }
        public void setPool(PoolConfig pool) { this.pool = pool; }
    }

    public static class PoolConfig {
        private int maxConnections;
        private int minIdle;
        private int connectionTimeout;

        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        public int getMinIdle() { return minIdle; }
        public void setMinIdle(int minIdle) { this.minIdle = minIdle; }
        public int getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    }

    public static class LoggingConfig {
        private String level;
        private boolean logMatches;
        private String logDir;

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public boolean isLogMatches() { return logMatches; }
        public void setLogMatches(boolean logMatches) { this.logMatches = logMatches; }
        public String getLogDir() { return logDir; }
        public void setLogDir(String logDir) { this.logDir = logDir; }
    }

    public static class IntegrationsConfig {
        private DiscordConfig discord;
        private PaymentsConfig payments;

        public DiscordConfig getDiscord() { return discord; }
        public void setDiscord(DiscordConfig discord) { this.discord = discord; }
        public PaymentsConfig getPayments() { return payments; }
        public void setPayments(PaymentsConfig payments) { this.payments = payments; }
    }

    public static class DiscordConfig {
        private boolean enabled;
        private String webhookUrl;
        private boolean postMatchSummary;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getWebhookUrl() { return webhookUrl; }
        public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
        public boolean isPostMatchSummary() { return postMatchSummary; }
        public void setPostMatchSummary(boolean postMatchSummary) { this.postMatchSummary = postMatchSummary; }
    }

    public static class PaymentsConfig {
        private boolean enabled;
        private String provider;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
    }

    public static class ExtrasConfig {
        private ApiConfig api;
        private SpectatorModeConfig spectatorMode;
        private AutoReportCheatConfig autoReportCheat;

        public ApiConfig getApi() { return api; }
        public void setApi(ApiConfig api) { this.api = api; }
        public SpectatorModeConfig getSpectatorMode() { return spectatorMode; }
        public void setSpectatorMode(SpectatorModeConfig spectatorMode) { this.spectatorMode = spectatorMode; }
        public AutoReportCheatConfig getAutoReportCheat() { return autoReportCheat; }
        public void setAutoReportCheat(AutoReportCheatConfig autoReportCheat) { this.autoReportCheat = autoReportCheat; }
    }

    public static class ApiConfig {
        private boolean enabled;
        private int port;
        private String token;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    public static class SpectatorModeConfig {
        private boolean enable;
        private List<String> allowedGroups;

        public boolean isEnable() { return enable; }
        public void setEnable(boolean enable) { this.enable = enable; }
        public List<String> getAllowedGroups() { return allowedGroups; }
        public void setAllowedGroups(List<String> allowedGroups) { this.allowedGroups = allowedGroups; }
    }

    public static class AutoReportCheatConfig {
        private boolean enabled;
        private double thresholdSuspicious;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public double getThresholdSuspicious() { return thresholdSuspicious; }
        public void setThresholdSuspicious(double thresholdSuspicious) { this.thresholdSuspicious = thresholdSuspicious; }
    }

    public static class DbSchemaConfig {
        private String players, stats, matches, seasons, bans, queues, penalties;

        public String getPlayers() { return players; }
        public void setPlayers(String players) { this.players = players; }
        public String getStats() { return stats; }
        public void setStats(String stats) { this.stats = stats; }
        public String getMatches() { return matches; }
        public void setMatches(String matches) { this.matches = matches; }
        public String getSeasons() { return seasons; }
        public void setSeasons(String seasons) { this.seasons = seasons; }
        public String getBans() { return bans; }
        public void setBans(String bans) { this.bans = bans; }
        public String getQueues() { return queues; }
        public void setQueues(String queues) { this.queues = queues; }
        public String getPenalties() { return penalties; }
        public void setPenalties(String penalties) { this.penalties = penalties; }
    }
}