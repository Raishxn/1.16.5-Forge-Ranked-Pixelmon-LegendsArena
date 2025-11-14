package com.raishxn.legendsarena.config;

import java.util.List;

// Mapeia o elos.yml
public class ElosConfig {

    // A chave principal "ranks" no seu YAML
    private List<EloRank> ranks;

    // Getters e Setters são necessários para o SnakeYAML
    public List<EloRank> getRanks() {
        return ranks;
    }

    public void setRanks(List<EloRank> ranks) {
        this.ranks = ranks;
    }

    // Esta é uma "classe interna" que representa cada item
    // dentro da lista "ranks" no YAML.
    public static class EloRank {
        private String name;
        private String prefix;
        private int minElo;
        private int maxElo;
        private String luckpermsGroup;

        // Getters e Setters para todos os campos
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPrefix() { return prefix; }
        public void setPrefix(String prefix) { this.prefix = prefix; }

        public int getMinElo() { return minElo; }
        public void setMinElo(int minElo) { this.minElo = minElo; }

        public int getMaxElo() { return maxElo; }
        public void setMaxElo(int maxElo) { this.maxElo = maxElo; }

        public String getLuckpermsGroup() { return luckpermsGroup; }
        public void setLuckpermsGroup(String luckpermsGroup) { this.luckpermsGroup = luckpermsGroup; }
    }
}