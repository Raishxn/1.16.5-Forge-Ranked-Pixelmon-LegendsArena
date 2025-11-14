package com.raishxn.legendsarena.config;

import java.util.List;

public class StatsConfig {

    // Nota: "stats-format" em YAML torna-se "statsFormat" em Java (camelCase)
    private List<String> statsFormat;
    private boolean eloHistory;

    public List<String> getStatsFormat() {
        return statsFormat;
    }

    public void setStatsFormat(List<String> statsFormat) {
        this.statsFormat = statsFormat;
    }

    public boolean isEloHistory() {
        return eloHistory;
    }

    public void setEloHistory(boolean eloHistory) {
        this.eloHistory = eloHistory;
    }
}