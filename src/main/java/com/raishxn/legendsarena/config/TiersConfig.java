package com.raishxn.legendsarena.config;

import java.util.List;

public class TiersConfig {

    private List<TierData> tiers;

    public List<TierData> getTiers() {
        return tiers;
    }

    public void setTiers(List<TierData> tiers) {
        this.tiers = tiers;
    }

    public static class TierData {
        private String id;
        private String name;
        private boolean enabled;
        private String permission;
        private BattleSpecs battleSpecs;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }

        public BattleSpecs getBattleSpecs() { return battleSpecs; }
        public void setBattleSpecs(BattleSpecs battleSpecs) { this.battleSpecs = battleSpecs; }
    }

    public static class BattleSpecs {
        private String battlespot;
        private int levelCap;
        private List<String> rules;

        public String getBattlespot() { return battlespot; }
        public void setBattlespot(String battlespot) { this.battlespot = battlespot; }

        public int getLevelCap() { return levelCap; }
        public void setLevelCap(int levelCap) { this.levelCap = levelCap; }

        public List<String> getRules() { return rules; }
        public void setRules(List<String> rules) { this.rules = rules; }
    }
}