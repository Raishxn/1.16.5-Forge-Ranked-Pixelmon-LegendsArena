package com.raishxn.legendsarena.config;

// Classe simples para "espelhar" o config.yml
public class CoreConfig {

    private DatabaseConfig database;
    private LoggingConfig logging;

    // Getters e Setters
    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }

    public LoggingConfig getLogging() {
        return logging;
    }

    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }

    // Classe interna para a seção "database"
    public static class DatabaseConfig {
        private String type;
        private String host;
        private int port;
        private String name;
        private String user;
        private String pass;

        // Getters e Setters
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
    }

    // Classe interna para a seção "logging"
    public static class LoggingConfig {
        private String level;
        private boolean logMatches;

        // Getters e Setters
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public boolean isLogMatches() { return logMatches; }
        public void setLogMatches(boolean logMatches) { this.logMatches = logMatches; }
    }
}