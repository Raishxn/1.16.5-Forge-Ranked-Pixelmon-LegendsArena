package com.raishxn.legendsarena.config;

import java.util.List;
import java.util.Map;

public class RewardsConfig {

    private Map<String, List<String>> endOfSeason;
    private Map<String, WinstreakReward> winstreak;

    public Map<String, List<String>> getEndOfSeason() {
        return endOfSeason;
    }

    public void setEndOfSeason(Map<String, List<String>> endOfSeason) {
        this.endOfSeason = endOfSeason;
    }

    public Map<String, WinstreakReward> getWinstreak() {
        return winstreak;
    }

    public void setWinstreak(Map<String, WinstreakReward> winstreak) {
        this.winstreak = winstreak;
    }

    public static class WinstreakReward {
        private int wins;
        private List<String> commands;

        public int getWins() { return wins; }
        public void setWins(int wins) { this.wins = wins; }

        public List<String> getCommands() { return commands; }
        public void setCommands(List<String> commands) { this.commands = commands; }
    }
}