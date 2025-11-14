package com.raishxn.legendsarena.config.models;

import java.util.Map;

public class Tier {
    private String id;
    private String displayName;
    private String bansFile;
    private Map<String, String> permissions;

    // --- Getters e Setters Completos ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBansFile() {
        return bansFile;
    }

    public void setBansFile(String bansFile) {
        this.bansFile = bansFile;
    }

    public Map<String, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, String> permissions) {
        this.permissions = permissions;
    }
}