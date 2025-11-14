package com.raishxn.legendsarena.config.models;

import java.util.List;

public class EloTier {
    private String id;
    private String displayName;
    private int minElo;
    private int maxElo;
    private List<String> subranks;
    private List<String> canPlayWith;
    private String luckpermsGroup;

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

    public int getMinElo() {
        return minElo;
    }

    public void setMinElo(int minElo) {
        this.minElo = minElo;
    }

    public int getMaxElo() {
        return maxElo;
    }

    public void setMaxElo(int maxElo) {
        this.maxElo = maxElo;
    }

    public List<String> getSubranks() {
        return subranks;
    }

    public void setSubranks(List<String> subranks) {
        this.subranks = subranks;
    }

    public List<String> getCanPlayWith() {
        return canPlayWith;
    }

    public void setCanPlayWith(List<String> canPlayWith) {
        this.canPlayWith = canPlayWith;
    }

    public String getLuckpermsGroup() {
        return luckpermsGroup;
    }

    public void setLuckpermsGroup(String luckpermsGroup) {
        this.luckpermsGroup = luckpermsGroup;
    }
}