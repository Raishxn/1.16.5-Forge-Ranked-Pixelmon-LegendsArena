package com.raishxn.legendsarena.config;

import java.util.List;

public class BattleRulesConfig {

    private DefaultRules defaultRules;

    public DefaultRules getDefaultRules() {
        return defaultRules;
    }

    public void setDefaultRules(DefaultRules defaultRules) {
        this.defaultRules = defaultRules;
    }

    public static class DefaultRules {
        private int teamSize;
        private int levelCap;
        private List<String> clauses;
        private boolean timer;

        public int getTeamSize() { return teamSize; }
        public void setTeamSize(int teamSize) { this.teamSize = teamSize; }

        public int getLevelCap() { return levelCap; }
        public void setLevelCap(int levelCap) { this.levelCap = levelCap; }

        public List<String> getClauses() { return clauses; }
        public void setClauses(List<String> clauses) { this.clauses = clauses; }

        public boolean isTimer() { return timer; }
        public void setTimer(boolean timer) { this.timer = timer; }
    }
}