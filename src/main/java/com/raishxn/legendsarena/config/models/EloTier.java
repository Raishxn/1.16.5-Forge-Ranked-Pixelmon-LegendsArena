package com.raishxn.legendsarena.config.models;

import java.util.List;

public class EloTier {

    private String name;
    private int minElo;
    private int maxElo;
    private List<SubTier> subTiers;

    // Getters e Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<SubTier> getSubTiers() {
        return subTiers;
    }

    public void setSubTiers(List<SubTier> subTiers) {
        this.subTiers = subTiers;
    }

    // Classe interna para os Sub-elos
    public static class SubTier {
        private String name; // Ex: "Gold I"
        private int minElo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMinElo() {
            return minElo;
        }

        public void setMinElo(int minElo) {
            this.minElo = minElo;
        }
    }
}