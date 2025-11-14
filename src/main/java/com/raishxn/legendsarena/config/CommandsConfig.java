package com.raishxn.legendsarena.config;

import java.util.Map;

public class CommandsConfig {

    private Map<String, CommandInfo> commands;

    public Map<String, CommandInfo> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, CommandInfo> commands) {
        this.commands = commands;
    }

    public static class CommandInfo {
        private String description;
        private String usage;
        private String permission;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getUsage() { return usage; }
        public void setUsage(String usage) { this.usage = usage; }

        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }
    }
}